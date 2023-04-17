package com.mineboundteam.minebound.particle.registry;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MineBound.MOD_ID);

    public static final RegistryObject<SimpleParticleType> FIRE_OFFENSIVE_1 =
            PARTICLES.register("fire_o1_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> FIRE_OFFENSIVE_2 =
            PARTICLES.register("fire_o2_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> FIRE_OFFENSIVE_3 =
            PARTICLES.register("fire_o3_particles", () -> new SimpleParticleType(true));
}
