package com.mineboundteam.minebound.magic.OffensiveSpells;

import com.mineboundteam.minebound.config.IConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import com.mineboundteam.minebound.magic.MagicType;
import com.mineboundteam.minebound.magic.SpellType;
import com.mineboundteam.minebound.particle.registry.ParticleRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FireOffensiveSpell extends ActiveSpellItem {

    private final int manaCost;
    private final Random rand = new Random();
    private static final HashMap<ArmorTier, RegistryObject<SimpleParticleType>> particles = new HashMap<>() {{
        put(ArmorTier.EFFIGY, ParticleRegistry.FIRE_OFFENSIVE_1);
        put(ArmorTier.SUIT, ParticleRegistry.FIRE_OFFENSIVE_2);
        put(ArmorTier.SYNERGY, ParticleRegistry.FIRE_OFFENSIVE_3);
    }};

    public FireOffensiveSpell(Properties properties, FireOffensiveSpellConfig config) {
        super(properties, config.LEVEL, MagicType.FIRE, SpellType.OFFENSIVE);

        this.manaCost = config.MANA_COST.get();
    }

    @Override
    public void use(ItemStack stack, Level level, Player player) {
    }

    @Override
    public void onUsingTick(ItemStack stack, Level level, Player player, int tickCount) {
        float yRot = player.getYRot();
        float xRot = player.getXRot();
        double x = (0 - Math.sin(yRot * Math.PI / 180f));
        double y = (0 - Math.sin(xRot * Math.PI / 180f));
        double z = (Math.cos(yRot * Math.PI / 180f));
        for (int i = 0; i < 360; i++) {
            if (i % 10 == 0) {
                level.addParticle(particles.get(this.level).get(),
                        player.getX() + x - (z * Math.sin(i) * (rand.nextDouble(0.075))),
                        player.getEyeY() - .25d + y + (Math.cos(i) * (rand.nextDouble(0.075))),
                        player.getZ() + z + (x * Math.sin(i) * (rand.nextDouble(0.075))),
                        x * 1000d - (Math.random() * (z * Math.sin(i) * 100d)),
                        y * 1000d + (Math.random() * (Math.cos(i) * 100d)),
                        z * 1000d + (Math.random() * (x * Math.sin(i) * 100d))
                );

                // TODO: look at ClientboundLevelParticlesPacket in order to make custom packet

//                    ServerLevel.sendParticles(particles.get(this.level).get(),
//                            player.getX() + x - (z * Math.sin(i) * (rand.nextDouble(0.075))),
//                            player.getEyeY() - .25d + y + (Math.cos(i) * (rand.nextDouble(0.075))),
//                            player.getZ() + z + (x * Math.sin(i) * (rand.nextDouble(0.075))),
//                            1,
//                            x / 10,
//                            y / 10,
//                            z / 10,
//                            10d
//                    );

            }
        }

        if (tickCount % 20 == 0) {
            reduceMana(manaCost, player);
        }
    }


    @Override
    public void releaseUsing(ItemStack stack, Level level, Player player) {
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static class FireOffensiveSpellConfig implements IConfig {

        public ForgeConfigSpec.IntValue MANA_COST;
        public ForgeConfigSpec.IntValue FIREBALL_MANA_COST;
        public ForgeConfigSpec.IntValue FIRE_DISTANCE;
        public ArmorTier LEVEL;
        private final int manaCost;
        private final int fireBallManaCost;
        private final int fireDistance;

        public FireOffensiveSpellConfig(int manaCost, int fireballManaCost, int fireDistanceInBlocks, ArmorTier level) {
            this.manaCost = manaCost;
            this.fireBallManaCost = fireballManaCost;
            this.fireDistance = fireDistanceInBlocks;
            this.LEVEL = level;
        }

        @Override
        public void build(ForgeConfigSpec.Builder builder) {
            builder.push("Offensive");
            builder.push(LEVEL.toString());
            MANA_COST = builder.comment("Mana cost").defineInRange("mana_cost", manaCost, 0, 10000);
            if (LEVEL == ArmorTier.SYNERGY) {
                FIREBALL_MANA_COST = builder.comment("Fireball mana cost").defineInRange("fireball_mana_cost", fireBallManaCost, 0, 10000);
            }
            FIRE_DISTANCE = builder.comment("Fire distance in blocks").defineInRange("fire_distance", fireDistance, 0, 10000);
            builder.pop(2);
        }

        @Override
        public void refresh(ModConfigEvent event) {

        }
    }
}
