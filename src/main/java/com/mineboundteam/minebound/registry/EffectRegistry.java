package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.effect.FireBarrierEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            MineBound.MOD_ID);

    public static final RegistryObject<MobEffect> FIRE_BARRIER = MOB_EFFECTS.register("fire_barrier",
            () -> new FireBarrierEffect(MobEffectCategory.BENEFICIAL, new Color(255, 119, 40).getRGB()));
}
