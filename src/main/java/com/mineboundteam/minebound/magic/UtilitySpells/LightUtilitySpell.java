package com.mineboundteam.minebound.magic.UtilitySpells;

import java.util.List;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider.UtilityToggle;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellType;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class LightUtilitySpell extends PassiveSpellItem implements ResourceManagerReloadListener {

    protected final LightUtilitySpellConfig config;

    
    // Having this local variable will be quicker than always retrieving the capability.
    // But, it's only safe to have a variable like this on client.
    @OnlyIn(Dist.CLIENT)
    public static boolean active = false;

    protected static PostChain entityEffect;

    protected static boolean isRendering = false;
    protected static boolean hasGlowing = false;
    protected static Minecraft minecraft = Minecraft.getInstance();

    public LightUtilitySpell(Properties properties, LightUtilitySpellConfig config) {
        super(properties, config.LEVEL, MagicType.LIGHT, SpellType.UTILITY);
        this.config = config;
        initOutline();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            LightUtilitySpell equippedSpell = getHighestSpellItem(LightUtilitySpell.class, player);
            if (equippedSpell != null) {
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("light_utility", -equippedSpell.config.MANA_REDUCTION.get()));
            } else {
                // Remove total mana reduction
                player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(playerMana -> playerMana.setManaCapModifier("light_utility", 0));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While equipped in a ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("utility slot").withStyle(ChatFormatting.DARK_PURPLE))
                                       .append(":"));
        pTooltipComponents.add(new TextComponent("  - Outlines mobs within a radius of ").withStyle(ChatFormatting.GRAY)
                                        .append(new TextComponent(config.OUTLINE_RADIUS.get() + " blocks.").withStyle(ChatFormatting.YELLOW)));
        pTooltipComponents.add(new TextComponent("Additional copies increase the ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("outline radius").withStyle(ChatFormatting.YELLOW)));
        pTooltipComponents.add(new TextComponent("Reduces ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("Manapool").withStyle(manaColorStyle))
                                       .append(" by ").append(new TextComponent(config.MANA_REDUCTION.get() + "").withStyle(manaColorStyle)));

        LocalPlayer player = minecraft.player;
        if(player != null) {
            MutableComponent active = new TextComponent("Mob outlining is currently ").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
            UtilityToggle toggle = player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).resolve().get();
            if(toggle != null && toggle.light)
                pTooltipComponents.add(active.append(new TextComponent("ON").withStyle(ChatFormatting.GREEN)));
            else
                pTooltipComponents.add(active.append(new TextComponent("OFF").withStyle(ChatFormatting.RED)));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void initOutline() {
        // At this point I don't care. It shouldn't be private.
        if (entityEffect == null) {
            try {
                var field = LevelRenderer.class.getDeclaredField("entityEffect");
                field.setAccessible(true);

                entityEffect = (PostChain) field.get(minecraft.levelRenderer);
            } catch (Exception e) {
                //i dont' care
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity, M extends EntityModel<T>> void renderEntity(RenderLivingEvent.Pre<T, M> event) {
        List<LightUtilitySpell> spells;
        if (!isRendering && active && !(spells = getEquippedSpellItemsOfType(LightUtilitySpell.class, minecraft.player)).isEmpty()) {
            LocalPlayer player = minecraft.player;
            T entity = (T) event.getEntity();
            LivingEntityRenderer<T, M> renderer = event.getRenderer();

            int viewRange = 0; 
            for (LightUtilitySpell spell : spells) {
                viewRange += spell.config.OUTLINE_RADIUS.get();
            }

            if (!entity.equals(player) && entity.distanceTo(player) < viewRange) {
                isRendering = true;
                if (entity.isCurrentlyGlowing()) {
                    hasGlowing = true;
                }

                OutlineBufferSource buff = minecraft.renderBuffers().outlineBufferSource();
                // TODO: make a client config to change the color
                buff.setColor(0xee, 0xee, 0xee, 255);

                renderer.render(entity, 0, event.getPartialTick(), event.getPoseStack(), buff, event.getPackedLight());
                isRendering = false;
                // Don't re-render if we already did once
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("removal")
    @OnlyIn(Dist.CLIENT)
    public static void renderOutline(net.minecraftforge.client.event.RenderLevelLastEvent event) {
        // This applies the actual outline. If one of the entities have glowing, because we
        // switch to an OutlineBufferSource for all entities, they'll all get glowing and we don't
        // want to apply it twice.
        if(entityEffect == null)
            initOutline();
        if (!hasGlowing) {
            minecraft.renderBuffers().outlineBufferSource().endOutlineBatch();
            entityEffect.process(event.getPartialTick());
            minecraft.getMainRenderTarget().bindWrite(false);
        }
        hasGlowing = false;
    }

    @Override
    // We need to refresh the entityEffect every time the resourcemanager reloads
    // We're basically borrowing minecraft's since it's the only one that'll work
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        entityEffect = null;
        initOutline();
    }

    public static class LightUtilitySpellConfig implements IConfig {
        public IntValue MANA_REDUCTION;
        public IntValue OUTLINE_RADIUS;
        public final ArmorTier LEVEL;
       
        private final int manaReduction;
        private final int outlineRadius;

        public LightUtilitySpellConfig(int manaReduction, int outlineRadius, ArmorTier level) {
            this.manaReduction = manaReduction;
            this.outlineRadius = outlineRadius;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Utility");
            builder.push(LEVEL.toString());
            MANA_REDUCTION = builder.comment("How much total mana will be reduced by").defineInRange("mana_reduction",
                    manaReduction, 0, 10000);
            OUTLINE_RADIUS = builder.comment("The number of blocks around the player that outline effect will be seen").defineInRange("outline_radius", outlineRadius, 0,
                    10000);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
