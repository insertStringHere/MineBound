package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.entity.FireProjectile;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.PlayerUtil;
import com.mineboundteam.minebound.util.StringUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FireOffensiveSpell extends ActiveSpellItem {

    private final FireOffensiveSpellConfig config;

    public FireOffensiveSpell(Properties properties, FireOffensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.FIRE, SpellType.OFFENSIVE);

        this.config = config;
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        if (config.SHOOT_FIREBALL.get()) {
            Vec3 lookAngle = player.getLookAngle();
            Vec3 handPos = PlayerUtil.getHandPos(player, usedHand);
            SmallFireball smallfireball = new SmallFireball(level,
                    handPos.x, handPos.y, handPos.z,
                    lookAngle.x, lookAngle.y, lookAngle.z);
            smallfireball.setOwner(player);
            level.addFreshEntity(smallfireball);
            level.playSound(player, player, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1f, 1f);
            reduceMana(config.FIREBALL_MANA_COST.get(), player);
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {
        Vec3 lookAngle = player.getLookAngle();
        Vec3 lookDirection = PlayerUtil.getLookDirection(player);
        Vec3 handPos = PlayerUtil.getHandPos(player, usedHand);
        for (int i = 0; i < 360; i += 80) {
            // Don't worry, I don't understand this math either, and I wrote the damn thing! - Matt
            double xOffset = lookAngle.x - (level.getRandom().nextDouble() * ((lookDirection.z * Math.sin(i)) + (lookDirection.y * lookDirection.x * Math.cos(i)))) / 5d;
            double zOffset = lookAngle.z + (level.getRandom().nextDouble() * ((lookDirection.x * Math.sin(i)) - (lookDirection.y * lookDirection.z * Math.cos(i)))) / 5d;
            level.addFreshEntity(new FireProjectile(level, player,
                    handPos.x, handPos.y, handPos.z,
                    xOffset,
                    lookAngle.y + (level.getRandom().nextDouble() * (Math.cos(i))) / 5d,
                    zOffset,
                    config.FIRE_DAMAGE.get(), config.IGNITE_BLOCKS.get(), config.FIRE_DISTANCE.get()
            ));
        }

        if (tickCount % 10 == 0) {
            level.playSound(player, player, SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1f, 1f);
            level.playSound(player, player, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.PLAYERS, 1f, 1f);
        }
        if (tickCount % 20 == 0) {
            reduceMana(config.MANA_COST.get(), player);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ColorUtil.Tooltip.defaultColor));
        pTooltipComponents.add(new TextComponent("  - Shoots a cone of fire ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.pluralize(config.FIRE_DISTANCE.get(), "block")).withStyle(ColorUtil.Tooltip.timeAndDistanceColor))
                .append(" that deals ")
                .append(new TextComponent(StringUtil.pluralize(config.FIRE_DAMAGE.get() / 2d, "heart") + " of damage").withStyle(ColorUtil.Tooltip.damageColor)));
        if (config.IGNITE_BLOCKS.get()) {
            pTooltipComponents.add(new TextComponent("  - Will ").withStyle(ColorUtil.Tooltip.defaultColor)
                    .append(new TextComponent("ignite").withStyle(ChatFormatting.GOLD))
                    .append(" blocks hit by the fire"));
        }
        if (config.SHOOT_FIREBALL.get()) {
            pTooltipComponents.add(new TextComponent("  - On initial cast, shoots a ").withStyle(ColorUtil.Tooltip.defaultColor)
                    .append(new TextComponent("fireball").withStyle(ChatFormatting.GOLD))
                    .append(" from the center of the cone"));
            pTooltipComponents.add(TooltipUtil.manaCost(config.FIREBALL_MANA_COST.get(), " to create the fireball"));
        }
        pTooltipComponents.add(TooltipUtil.manaCost(config.MANA_COST.get(), " per second of use"));
    }

    public static class FireOffensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.IntValue FIRE_DISTANCE;
        public ForgeConfigSpec.DoubleValue FIRE_DAMAGE;
        public ForgeConfigSpec.BooleanValue IGNITE_BLOCKS;
        public ForgeConfigSpec.BooleanValue SHOOT_FIREBALL;
        public ForgeConfigSpec.IntValue FIREBALL_MANA_COST;
        public ArmorTier LEVEL;
        private final int manaCost;
        private final int fireDistance;
        private final double fireDamage;
        private final boolean igniteBlocks;
        private final boolean shootFireball;
        private final int fireBallManaCost;

        public FireOffensiveSpellConfig(int manaCost, int fireDistanceInBlocks, double fireDamage, boolean igniteBlocks, boolean shootFireball, int fireballManaCost, ArmorTier level) {
            this.manaCost = manaCost;
            this.fireDistance = fireDistanceInBlocks;
            this.fireDamage = fireDamage;
            this.igniteBlocks = igniteBlocks;
            this.shootFireball = shootFireball;
            this.fireBallManaCost = fireballManaCost;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            FIRE_DISTANCE = builder.comment("Fire distance in blocks").defineInRange("fire_distance", fireDistance, 0, 10000);
            FIRE_DAMAGE = builder.comment("Damage done by fire").defineInRange("fire_damage", fireDamage, 0, 10000);
            IGNITE_BLOCKS = builder.comment("Fire from spell will ignite blocks").define("ignite_blocks", igniteBlocks);
            SHOOT_FIREBALL = builder.comment("Shoot a fireball on initial cast").define("shoot_fireball", shootFireball);
            FIREBALL_MANA_COST = builder.comment("Fireball mana cost").defineInRange("fireball_mana_cost", fireBallManaCost, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
