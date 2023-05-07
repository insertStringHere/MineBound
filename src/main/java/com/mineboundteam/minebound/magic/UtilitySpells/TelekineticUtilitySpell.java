package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class TelekineticUtilitySpell extends PassiveSpellItem {

    private final int manaCost;
    private final double manaCostReduction;
    private final boolean elytraFlight;

    public TelekineticUtilitySpell(Properties properties, TelekineticUtilitySpellConfig config) {
        super(properties, config.LEVEL, MagicType.TELEKINETIC, SpellType.UTILITY);

        manaCost = config.MANA_COST.get();
        manaCostReduction = config.MANA_COST_REDUCTION.get();
        this.elytraFlight = config.ELYTRA_FLIGHT.get();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START && !event.player.isCreative() && !event.player.isSpectator()) {
            Player player = event.player;
            List<TelekineticUtilitySpell> equippedSpells = getEquippedSpellItemsOfType(TelekineticUtilitySpell.class, event.player);

            // Set if player should still have flight on every tick
            player.getAbilities().mayfly = equippedSpells.size() > 0;
            if (!player.getAbilities().mayfly) {
                player.getAbilities().flying = false;
            }
            player.onUpdateAbilities();

            CompoundTag elytraTag = player.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag();
            if (equippedSpells.size() > 0) {
                TelekineticUtilitySpell highestLevelSpell = getHighestSpellItem(equippedSpells);
                elytraTag.putBoolean("minebound.telekinetic_utility.elytra_flight", highestLevelSpell.elytraFlight);

                // Calculate mana cost only once per second (every 20 ticks) if player is flying
                if (player.level.getGameTime() % 20 == 0 && (player.getAbilities().flying || player.isFallFlying())) {
                    equippedSpells.remove(highestLevelSpell);
                    int reducedManaCost = highestLevelSpell.manaCost;
                    for (TelekineticUtilitySpell spell : equippedSpells) {
                        reducedManaCost *= 1 - spell.manaCostReduction;
                    }
                    reduceMana(reducedManaCost, player);
                }
            } else {
                elytraTag.putBoolean("minebound.telekinetic_utility.elytra_flight", false);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void triggerSpell(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof Player player && !player.getLevel().isClientSide()
                && (event.getSource() == DamageSource.FALL || event.getSource() == DamageSource.FLY_INTO_WALL)) {
            TelekineticUtilitySpell spell = getHighestSpellItem(TelekineticUtilitySpell.class, player);
            if (spell != null) {
                event.setCanceled(true);
                reduceMana(spell.manaCost, player);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("  - Gives creative flight").withStyle(ColorUtil.Tooltip.defaultColor));
        if (elytraFlight) {
            pTooltipComponents.add(new TextComponent("  - Gives elytra flight (must have a Myrial Chestpiece equipped)")
                    .withStyle(ColorUtil.Tooltip.defaultColor));
        }
        pTooltipComponents.add(TooltipUtil.manaCost(manaCost, " per second of flight"));
        pTooltipComponents.add(new TextComponent("Additional copies instead reduce ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("Mana").withStyle(ColorUtil.Tooltip.manaColorStyle))
                .append(" cost of highest equipped tier by ")
                .append(new TextComponent(StringUtil.percentage(manaCostReduction)).withStyle(ColorUtil.Tooltip.reductionColorStyle))
                .append(" each"));
    }

    public static class TelekineticUtilitySpellConfig implements IConfig {

        public IntValue MANA_COST;
        public DoubleValue MANA_COST_REDUCTION;
        public ForgeConfigSpec.BooleanValue ELYTRA_FLIGHT;
        public final ArmorTier LEVEL;
        private final int manaCost;
        private final double manaCostReduction;
        private final boolean elytraFlight;

        public TelekineticUtilitySpellConfig(int manaCost, double manaCostReduction, boolean elytraFlight, ArmorTier level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
            this.manaCostReduction = manaCostReduction;
            this.elytraFlight = elytraFlight;
        }

        @Override
        public void build(Builder builder) {
            builder.push("Utility");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            MANA_COST_REDUCTION = builder.comment("Mana cost reduction multiplier").defineInRange("mana_cost_reduction", manaCostReduction, 0.0, 1.0);
            ELYTRA_FLIGHT = builder.comment("Gives elytra flight").define("elytra_flight", elytraFlight);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
