package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;


@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class TelekineticUtilitySpell extends PassiveSpellItem {

    private final int manaCost;
    private final double manaCostReduction;

    public TelekineticUtilitySpell(Properties properties, TelekineticUtilitySpellConfig config) {
        super(properties, config.LEVEL);

        manaCost = config.MANA_COST.get();
        manaCostReduction = config.MANA_COST_REDUCTION.get();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START && !event.player.isCreative()) {
            Player player = event.player;
            List<TelekineticUtilitySpell> equippedSpells = getEquippedSpellItemsOfType(TelekineticUtilitySpell.class, event.player);
            TelekineticUtilitySpell highestLevelSpell = getHighestSpellItem(equippedSpells);

            // Set if player should still have flight on every tick
            player.getAbilities().mayfly = equippedSpells.size() > 0;
            if (!player.getAbilities().mayfly) {
                player.getAbilities().flying = false;
            }
            player.onUpdateAbilities();

            CompoundTag elytraTag = new CompoundTag();
            if (equippedSpells.size() > 0) {
                elytraTag.putBoolean("minebound.telekinetic_utility.elytra_flight", highestLevelSpell.level.getValue() >= ArmorTier.SYNERGY.getValue());
                player.getItemBySlot(EquipmentSlot.CHEST).setTag(elytraTag);

                // Calculate mana cost only once per second (every 20 ticks) if player is flying
                if (player.level.getGameTime() % 20 == 0 && (player.getAbilities().flying || player.isFallFlying())) {
                    equippedSpells.remove(highestLevelSpell);
                    int reducedManaCost = highestLevelSpell.manaCost;
                    for (TelekineticUtilitySpell spell : equippedSpells) {
                        reducedManaCost *= 1 - spell.manaCostReduction;
                    }
                    highestLevelSpell.reduceMana(reducedManaCost, player);
                }
            } else {
                elytraTag.putBoolean("minebound.telekinetic_utility.elytra_flight", false);
                player.getItemBySlot(EquipmentSlot.CHEST).setTag(elytraTag);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("  - Gives creative flight").withStyle(ChatFormatting.GRAY));
        if (this.level.getValue() >= ArmorTier.SYNERGY.getValue()) {
            pTooltipComponents.add(new TextComponent("  - Gives elytra flight (must have a Myrial Chestpiece equipped)").withStyle(ChatFormatting.GRAY));
        }
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       // TODO: Color subject to change once mana UI is implemented
                                       .append(new TextComponent(manaCost + " Mana").withStyle(ChatFormatting.BLUE))
                                       .append(" per second of flight"));
        pTooltipComponents.add(new TextComponent("Additional copies instead reduce Mana cost of highest equipped tier by ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent((int) (manaCostReduction * 100) + "%").withStyle(ChatFormatting.BLUE))
                                       .append(" each"));
    }

    public static class TelekineticUtilitySpellConfig implements IConfig {

        public IntValue MANA_COST;
        public DoubleValue MANA_COST_REDUCTION;
        public final ArmorTier LEVEL;
        private final int manaCost;
        private final double manaCostReduction;

        public TelekineticUtilitySpellConfig(int manaCost, double manaCostReduction, ArmorTier level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
            this.manaCostReduction = manaCostReduction;
        }

        @Override
        public void build(Builder builder) {
            builder.push("Utility");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            MANA_COST_REDUCTION = builder.comment("Mana cost reduction multiplier").defineInRange("mana_cost_reduction", manaCostReduction, 0.0, 1.0);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
