package com.mineboundteam.minebound.magic.DefensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class ShieldDefensiveSpell extends ActiveSpellItem {
    public static final String ACTIVE_TAG = "minebound.shield_defensive.active";

    private final ShieldDefensiveSpellConfig config;

    public ShieldDefensiveSpell(Properties properties, ShieldDefensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.SHIELD, SpellType.DEFENSIVE);

        this.config = config;
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, true);
    }

    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {

    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, false);
    }

    @SubscribeEvent
    public static void triggerSpell(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayer player && event.getSource() != DamageSource.STARVE && !event.getSource().isBypassInvul()) {
            boolean spellTriggered = triggerSpell(player, getSelectedSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL), event);
            if (!spellTriggered) {
                spellTriggered = triggerSpell(player, getSelectedSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL), event);
            }
            if (!spellTriggered) {
                triggerSpell(player, player.getUseItem(), event);
            }
        }
    }

    protected static boolean triggerSpell(Player player, ItemStack selectedSpell, LivingAttackEvent event) {
        if (selectedSpell.getItem() instanceof ShieldDefensiveSpell spell) {
            if (selectedSpell.getOrCreateTag().getBoolean(ACTIVE_TAG)) {
                event.setCanceled(true);
                player.getLevel().playSound(null, player, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1f, 1f);
                reduceMana(spell.config.MANA_COST.get(), player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ColorUtil.Tooltip.defaultColor));
        pTooltipComponents.add(new TextComponent("  - Blocks incoming damage from all directions like a shield").withStyle(ColorUtil.Tooltip.defaultColor));
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST.get(), " each time damage is blocked"));
    }

    public static class ShieldDefensiveSpellConfig implements IConfig {
        public ForgeConfigSpec.IntValue MANA_COST;
        public final ArmorTier LEVEL;
        private final int manaCost;

        public ShieldDefensiveSpellConfig(int manaCost, ArmorTier level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Defensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost per block").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
