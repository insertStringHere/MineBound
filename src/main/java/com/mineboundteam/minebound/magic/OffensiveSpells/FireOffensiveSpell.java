package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.entity.FireProjectile;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
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
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class FireOffensiveSpell extends ActiveSpellItem {

    private final int manaCost;
    private final int fireDistance;
    private final double fireDamage;
    private final boolean igniteBlocks;
    private final boolean shootFireball;
    private final int fireballManaCost;

    public FireOffensiveSpell(Properties properties, FireOffensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.FIRE, SpellType.OFFENSIVE);

        this.manaCost = config.MANA_COST.get();
        this.fireDistance = config.FIRE_DISTANCE.get();
        this.fireDamage = config.FIRE_DAMAGE.get();
        this.igniteBlocks = config.IGNITE_BLOCKS.get();
        this.shootFireball = config.SHOOT_FIREBALL.get();
        this.fireballManaCost = config.FIREBALL_MANA_COST.get();
    }

    @Override
    public void use(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
        if (shootFireball) {
            float yRot = player.getYRot();
            float xRot = player.getXRot();
            double x = (0 - Math.sin(yRot * Math.PI / 180f));
            double y = (0 - Math.sin(xRot * Math.PI / 180f));
            double z = (Math.cos(yRot * Math.PI / 180f));
            // right hand = 1
            // left hand = -1
            int hand = usedHand == InteractionHand.MAIN_HAND ? 1 : -1;
            SmallFireball smallfireball = new SmallFireball(level,
                    player.getX() - (z / 2.5d * hand),
                    player.getEyeY() - 1d,
                    player.getZ() + (x / 2.5d * hand),
                    x, y, z);
            smallfireball.setOwner(player);
            level.addFreshEntity(smallfireball);
            level.playSound(null, player.getX(), player.getEyeY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1f, 1f);
            reduceMana(fireballManaCost, player);
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, InteractionHand usedHand, Level level, Player player, int tickCount) {
        float yRot = player.getYRot();
        float xRot = player.getXRot();
        double x = (0 - Math.sin(yRot * Math.PI / 180f));
        double y = (0 - Math.sin(xRot * Math.PI / 180f));
        double z = (Math.cos(yRot * Math.PI / 180f));
        // right hand = 1
        // left hand = -1
        int hand = usedHand == InteractionHand.MAIN_HAND ? 1 : -1;
        for (int i = 0; i <= 360; i += 40) {
            level.addFreshEntity(new FireProjectile(level, player,
                    player.getX() - (z / 2.5d * hand),
                    player.getEyeY() - 1d,
                    player.getZ() + (x / 2.5d * hand),
                    x - (level.getRandom().nextDouble() * (z * Math.sin(i))) / 5d,
                    y + (level.getRandom().nextDouble() * Math.cos(i)) / 5d,
                    z + (level.getRandom().nextDouble() * (x * Math.sin(i))) / 5d,
                    fireDamage,
                    igniteBlocks,
                    fireDistance
            ));
        }

        if (tickCount % 10 == 0) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1f, 1f);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CAMPFIRE_CRACKLE, SoundSource.PLAYERS, 1f, 1f);
        }
        if (tickCount % 20 == 0) {
            reduceMana(manaCost, player);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, InteractionHand usedHand, Level level, Player player) {
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While active:").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TextComponent("  - Shoots a cone of fire ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent(fireDistance + " blocks").withStyle(ChatFormatting.DARK_GREEN))
                .append(" that deals ")
                .append(new TextComponent(new DecimalFormat("0.#").format(fireDamage) + " hearts of damage").withStyle(ChatFormatting.RED)));
        if (igniteBlocks) {
            pTooltipComponents.add(new TextComponent("  - Will ").withStyle(ChatFormatting.GRAY)
                    .append(new TextComponent("ignite").withStyle(ChatFormatting.GOLD))
                    .append(" blocks hit by the fire"));
        }
        if (shootFireball) {
            pTooltipComponents.add(new TextComponent("  - On initial cast, shoots a ").withStyle(ChatFormatting.GRAY)
                    .append(new TextComponent("fireball").withStyle(ChatFormatting.GOLD))
                    .append(" from the center of the cone"));
            pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                    .append(new TextComponent(fireballManaCost + " Mana").withStyle(manaColorStyle))
                    .append(" to create the fireball"));
        }
        pTooltipComponents.add(new TextComponent("Costs ").withStyle(ChatFormatting.GRAY)
                .append(new TextComponent(manaCost + " Mana").withStyle(manaColorStyle))
                .append(" per second of use"));
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
