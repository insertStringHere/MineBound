package com.mineboundteam.minebound.magic.DefensiveSpells;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.effect.ElectricDebuff;
import com.mineboundteam.minebound.effect.registry.EffectRegistry;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
public class ElectricDefensiveSpell extends ActiveSpellItem {
    public static final String ACTIVE_TAG = "minebound.electric_defensive.active";

    private final int initialManaCost;
    private final int manaCostPerHit;
    private final int durationInTicks;

    public ElectricDefensiveSpell(Properties properties, ElectricDefensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.ELECTRIC, SpellType.DEFENSIVE);

        this.initialManaCost = config.INITIAL_MANA_COST.get();
        this.manaCostPerHit = config.MANA_COST_PER_HIT.get();
        this.durationInTicks = config.DURATION_TICKS.get();
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        stack.getOrCreateTag().putBoolean(ACTIVE_TAG, true);
        reduceMana(initialManaCost, player);
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
        if (event.getEntityLiving() instanceof Player player && !player.getLevel().isClientSide() && event.getSource().getEntity() instanceof LivingEntity entity) {
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
        if (selectedSpell.getItem() instanceof ElectricDefensiveSpell spell) {
            if (selectedSpell.getOrCreateTag().getBoolean(ACTIVE_TAG)) {
                entity.addEffect(new MobEffectInstance(EffectRegistry.ELECTRIC_DEBUFF.get(), spell.durationInTicks, spell.level.getValue()));
                reduceMana(spell.manaCostPerHit, player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("  - When hit by a mob, ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent("Electric Debuff ").withStyle(Style.EMPTY.withColor(EffectRegistry.ELECTRIC_DEBUFF.get().getColor()))
                        .append(new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + level.getValue())))
                .append(" will be applied to that mob for ")
                .append(new TextComponent(durationInTicks / 20 + " seconds").withStyle(ChatFormatting.DARK_GREEN)));
        pTooltipComponents.add(new TextComponent("  - ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent("Electric Debuff ").withStyle(Style.EMPTY.withColor(EffectRegistry.ELECTRIC_DEBUFF.get().getColor()))
                        .append(new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + level.getValue())))
                .append(" applies ")
                .append(new TextComponent("Slowness ").withStyle(Style.EMPTY.withColor(MobEffects.MOVEMENT_SLOWDOWN.getColor()))
                        .append(new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + ElectricDebuff.getSlownessLevel(level.getValue()))))
                .append(" and increases vulnerability to ")
                .append(new TextComponent("electricity").withStyle(Style.EMPTY.withColor(EffectRegistry.ELECTRIC_DEBUFF.get().getColor())))
                .append(" by ")
                .append(new TextComponent(String.format("%.0f%%", (ElectricDebuff.getDmgMult(level.getValue()) - 1) * 100)).withStyle(ChatFormatting.RED)));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent(initialManaCost + " Mana").withStyle(manaColorStyle))
                .append(" on initial cast"));
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent(manaCostPerHit + " Mana").withStyle(manaColorStyle))
                .append(" each time the player is hit"));
    }

    public static class ElectricDefensiveSpellConfig implements IConfig {
        ForgeConfigSpec.IntValue INITIAL_MANA_COST;
        ForgeConfigSpec.IntValue MANA_COST_PER_HIT;
        ForgeConfigSpec.IntValue DURATION_TICKS;
        public final ArmorTier LEVEL;

        private final int initialManaCost;
        private final int manaCostPerHit;
        private final int durationInTicks;

        public ElectricDefensiveSpellConfig(int initialManaCost, int manaCostPerHit, int durationInTicks, ArmorTier level) {
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
            DURATION_TICKS = builder.comment("Duration the electric debuff effect lasts for in ticks (20 ticks = 1 second)").defineInRange("duration_ticks", durationInTicks, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
