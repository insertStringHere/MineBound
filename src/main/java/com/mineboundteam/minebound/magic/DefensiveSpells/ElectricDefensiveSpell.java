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
public class ElectricDefensiveSpell extends ActiveSpellItem {
    public static final String ACTIVE_TAG = "minebound.electric_defensive.active";

    private final ElectricDefensiveSpellConfig config;

    public ElectricDefensiveSpell(Properties properties, ElectricDefensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.ELECTRIC, SpellType.DEFENSIVE);

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
        if (event.getEntityLiving() instanceof ServerPlayer player && event.getSource().getEntity() instanceof LivingEntity entity && !player.is(entity)) {
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
                entity.addEffect(new MobEffectInstance(EffectRegistry.ELECTRIC_DEBUFF.get(), spell.config.DURATION_TICKS.get(), spell.level.getValue()), player);
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
        pTooltipComponents.add(new TextComponent("  - When hit by a mob, ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("Electric Debuff ").withStyle(ColorUtil.Tooltip.color(ColorUtil.ELECTRIC_DEBUFF_COLOR))
                        .append(TooltipUtil.level(level.getValue())))
                .append(" will be applied to that mob for ")
                .append(new TextComponent(StringUtil.pluralize(config.DURATION_TICKS.get() / 20, "second")).withStyle(ColorUtil.Tooltip.timeAndDistanceColor)));
        pTooltipComponents.add(new TextComponent("  - ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("Electric Debuff ").withStyle(ColorUtil.Tooltip.color(ColorUtil.ELECTRIC_DEBUFF_COLOR))
                        .append(TooltipUtil.level(level.getValue())))
                .append(" applies ")
                .append(new TextComponent("Slowness ").withStyle(ColorUtil.Tooltip.effectColor(MobEffects.MOVEMENT_SLOWDOWN))
                        .append(TooltipUtil.level(ElectricDebuff.getSlownessLevel(level.getValue()))))
                .append(" and increases vulnerability to ")
                .append(new TextComponent("electricity").withStyle(ColorUtil.Tooltip.color(ColorUtil.ELECTRIC_DEBUFF_COLOR)))
                .append(" by ")
                .append(new TextComponent(StringUtil.percentage(ElectricDebuff.getDmgMult(level.getValue()) - 1)).withStyle(ColorUtil.Tooltip.damageColor)));
        pTooltipComponents.add(TooltipUtil.manaCost(config.INITIAL_MANA_COST.get(), " on initial cast"));
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST_PER_HIT.get(), " each time the player is hit"));
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
