package com.mineboundteam.minebound.effect;

import com.mineboundteam.minebound.MineBound;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.Color;

public class ModEffects {
  public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
      MineBound.MOD_ID);

  public static final RegistryObject<MobEffect> FIRE_BARRIER = MOB_EFFECTS.register("fire_barrier",
      () -> new FireBarrierEffect(MobEffectCategory.BENEFICIAL, new Color(255, 119, 40).getRGB()));

  public static void register(IEventBus eventBus) {
    MOB_EFFECTS.register(eventBus);
  }
}