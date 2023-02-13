package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.PassiveSpellItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;


@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class TelekineticUtilitySpell extends PassiveSpellItem {

    private final int manaCost;
    private final ArmorTier level;

    static HashMap<ArmorTier, Double> manaCostReductions = new HashMap<>() {{
        put(ArmorTier.SUIT, 0.20);
        put(ArmorTier.SYNERGY, 0.35);
        put(ArmorTier.SINGULARITY, 0.5);
    }};

    public TelekineticUtilitySpell(Properties properties, TelekineticUtilitySpellConfig config) {
        super(properties, config.LEVEL);

        manaCost = config.MANA_COST.get();
        level = config.LEVEL;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START && !event.player.isCreative()) {
            Player player = event.player;
            List<TelekineticUtilitySpell> equippedTelekineticUtilitySpells = getEquippedSpellsOfType(TelekineticUtilitySpell.class, event.player);

            // Set if player should still have flight on every tick
            player.getAbilities().mayfly = equippedTelekineticUtilitySpells.size() > 0;
            if (!player.getAbilities().mayfly) {
                player.getAbilities().flying = false;
            }
            player.onUpdateAbilities();

            // Calculate mana cost only once per second (every 20 ticks) if player is flying
            if (player.level.getDayTime() % 20 == 0 && equippedTelekineticUtilitySpells.size() > 0 && player.getAbilities().flying) {
                TelekineticUtilitySpell highestLevelSpell = equippedTelekineticUtilitySpells.get(0);
                for (TelekineticUtilitySpell equippedTelekineticUtilitySpell : equippedTelekineticUtilitySpells.subList(1, equippedTelekineticUtilitySpells.size())) {
                    if (equippedTelekineticUtilitySpell.level.getValue() > highestLevelSpell.level.getValue()) {
                        highestLevelSpell = equippedTelekineticUtilitySpell;
                    }
                }
                equippedTelekineticUtilitySpells.remove(highestLevelSpell);
                int reducedManaCost = highestLevelSpell.manaCost;
                for (TelekineticUtilitySpell equippedTelekineticUtilitySpell : equippedTelekineticUtilitySpells) {
                    reducedManaCost *= 1 - manaCostReductions.get(equippedTelekineticUtilitySpell.level);
                }
                // Will cost as little as 1 mana per second
                highestLevelSpell.reduceMana(Math.max(reducedManaCost, 1), player);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent("While equipped in utility slot:").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("  - Gives creative flight").withStyle(ChatFormatting.GRAY));
        if (this.level.getValue() >= ArmorTier.SINGULARITY.getValue()) {
            pTooltipComponents.add(new TextComponent("  - Gives elytra flight"));
        }
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                                       // TODO: Color subject to change once mana UI is implemented
                                       .append(new TextComponent(manaCost + " Mana").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.UNDERLINE))
                                       .append(" per second of flight").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("Additional copies reduce Mana cost by ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent((int) (manaCostReductions.get(this.level) * 100) + "%").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.UNDERLINE)));
    }

    public static class TelekineticUtilitySpellConfig implements IConfig {

        public IntValue MANA_COST;
        public final ArmorTier LEVEL;

        private final int manaCost;

        public TelekineticUtilitySpellConfig(int manaCost, ArmorTier level) {
            this.manaCost = manaCost;
            this.LEVEL = level;
        }

        @Override
        public void build(Builder builder) {
            builder.push("Telekinetic Utility");
            builder.push("Level " + LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            builder.pop();
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
