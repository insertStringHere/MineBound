package com.mineboundteam.minebound.magic.DefensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class EnderDefensiveSpell extends ActiveSpellItem {
    public static final String ACTIVE_TAG = "minebound.ender_defensive.active";

    private final EnderDefensiveSpellConfig config;

    public EnderDefensiveSpell(Properties properties, EnderDefensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.ENDER, SpellType.DEFENSIVE);

        this.config = config;
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, true);
        reduceMana(config.INITIAL_MANA_COST.get(), player);
    }

    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {
    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, false);
    }

    @SubscribeEvent
    public static void triggerSpell(LivingDamageEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayer player && event.getSource().getEntity() instanceof LivingEntity entity) {
            boolean spellTriggered = triggerSpell(player, entity, getSelectedSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL));
            if (!spellTriggered) {
                spellTriggered = triggerSpell(player, entity, getSelectedSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL));
            }
            if (!spellTriggered) {
                triggerSpell(player, entity, player.getUseItem());
            }
        }
    }

    protected static boolean triggerSpell(Player player, LivingEntity entity, ItemStack selectedSpell) {
        if (selectedSpell.getItem() instanceof EnderDefensiveSpell spell && selectedSpell.hasTag()) {
            boolean isActive = selectedSpell.getOrCreateTag().getBoolean(ACTIVE_TAG);
            if (isActive) {
                entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, spell.config.DURATION_TICKS.get(), 0, false, false), player);
                reduceMana(spell.config.MANA_COST_PER_HIT.get(), player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ColorUtil.Tooltip.defaultColor));
        pTooltipComponents.add(new TextComponent("  - When hit by a mob, that mob will ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("levitate").withStyle(ColorUtil.Tooltip.effectColor(MobEffects.LEVITATION)))
                .append(" for ")
                .append(new TextComponent(StringUtil.pluralize(config.DURATION_TICKS.get() / 20d, "second")).withStyle(ColorUtil.Tooltip.timeAndDistanceColor)));
        pTooltipComponents.add(TooltipUtil.manaCost(config.INITIAL_MANA_COST.get(), " on initial cast"));
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST_PER_HIT.get(), " each time the player is hit"));
    }

    public static class EnderDefensiveSpellConfig implements IConfig {
        ForgeConfigSpec.IntValue INITIAL_MANA_COST;
        ForgeConfigSpec.IntValue MANA_COST_PER_HIT;
        ForgeConfigSpec.IntValue DURATION_TICKS;
        public final ArmorTier LEVEL;

        private final int initialManaCost;
        private final int manaCostPerHit;
        private final int durationInTicks;

        public EnderDefensiveSpellConfig(int initialManaCost, int manaCostPerHit, int durationInTicks, ArmorTier level) {
            this.initialManaCost = initialManaCost;
            this.manaCostPerHit = manaCostPerHit;
            this.durationInTicks = durationInTicks;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Defensive");
            builder.push(LEVEL.toString());
            INITIAL_MANA_COST = builder.comment("Mana cost on initial use").defineInRange("initial_mana_cost", initialManaCost, 0, 10000);
            MANA_COST_PER_HIT = builder.comment("Mana cost each time player is hit while active").defineInRange("mana_cost_per_hit", manaCostPerHit, 0, 10000);
            DURATION_TICKS = builder.comment("Duration the levitation effect lasts for in ticks (20 ticks = 1 second)").defineInRange("duration_ticks", durationInTicks, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
