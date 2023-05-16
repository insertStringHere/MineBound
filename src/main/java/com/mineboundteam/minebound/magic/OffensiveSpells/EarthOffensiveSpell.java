package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.entity.RockProjectile;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.PlayerUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class EarthOffensiveSpell extends ActiveSpellItem {
    private final EarthOffensiveSpellConfig config;

    public EarthOffensiveSpell(Properties properties, EarthOffensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.EARTH, SpellType.OFFENSIVE);

        this.config = config;
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        Vec3 lookAngle = player.getLookAngle();
        Vec3 handPos = PlayerUtil.getHandPos(player, usedHand);
        RockProjectile rockProjectile = new RockProjectile(level, player,
                handPos.x, handPos.y, handPos.z,
                config.DAMAGE.get().floatValue(), config.AOE_RANGE.get(),
                config.SLOW_ON_HIT.get(), config.SLOW_DURATION.get()
        );
        rockProjectile.shoot(
                lookAngle.x,
                lookAngle.y + 0.25d,
                lookAngle.z,
                1.5f, 0f
        );
        level.addFreshEntity(rockProjectile);
        reduceMana(config.MANA_COST.get(), player);
    }

    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {
    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("When activated:").withStyle(ColorUtil.Tooltip.defaultColor));
        pTooltipComponents.add(new TextComponent("  - Launches a ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("Rock").withStyle(ColorUtil.Tooltip.color(new Color(193, 154, 116).getRGB())))
                .append(" that upon impact: "));
        pTooltipComponents.add(new TextComponent("    - Will deal ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.pluralize(config.DAMAGE.get() / 2d, "heart") + " of damage").withStyle(ColorUtil.Tooltip.damageColor))
                .append(" to all mobs in a ")
                .append(new TextComponent(config.AOE_RANGE.get() + " block radius").withStyle(ColorUtil.Tooltip.timeAndDistanceColor)));
        if (config.SLOW_ON_HIT.get()) {
            pTooltipComponents.add(new TextComponent("    - Will apply ").withStyle(ColorUtil.Tooltip.defaultColor)
                    .append(new TextComponent("Slowness").withStyle(ColorUtil.Tooltip.effectColor(MobEffects.MOVEMENT_SLOWDOWN)))
                    .append(" to all mobs in a ")
                    .append(new TextComponent(config.AOE_RANGE.get() + " block radius").withStyle(ColorUtil.Tooltip.timeAndDistanceColor)));
        }
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST.get(), " per use"));
    }

    public static class EarthOffensiveSpellConfig implements IConfig {
        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.DoubleValue DAMAGE;
        public ForgeConfigSpec.IntValue AOE_RANGE;
        public ForgeConfigSpec.BooleanValue SLOW_ON_HIT;
        public ForgeConfigSpec.IntValue SLOW_DURATION;
        public final ArmorTier LEVEL;
        private final int manaCost;
        private final double damage;
        private final int aoeRange;
        private final boolean slowOnHit;
        private final int slowDuration;

        public EarthOffensiveSpellConfig(int manaCost, double damage, int aoeRange, boolean slowOnHit, int slowDuration, ArmorTier level) {
            this.manaCost = manaCost;
            this.damage = damage;
            this.aoeRange = aoeRange;
            this.slowOnHit = slowOnHit;
            this.slowDuration = slowDuration;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            DAMAGE = builder.comment("Damage the rock projectile will deal in the AOE (2 damage = 1 heart)").defineInRange("damage", damage, 0, 10000);
            AOE_RANGE = builder.comment("The range from the point of impact where damage will be dealt").defineInRange("aoe_range", aoeRange, 0, 10000);
            SLOW_ON_HIT = builder.comment("Should slow hit enemies").define("slow_on_hit", slowOnHit);
            SLOW_DURATION = builder.comment("How long the slow effect lasts in ticks (20 ticks = 1 second)").defineInRange("slow_duration", slowDuration, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
