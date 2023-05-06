package com.mineboundteam.minebound.effect.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.effect.ElectricDebuff;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            MineBound.MOD_ID);

    public static final RegistryObject<MobEffect> ELECTRIC_DEBUFF = MOB_EFFECTS.register("electric_debuff",
            () -> new ElectricDebuff(MobEffectCategory.HARMFUL, new Color(234, 232, 166).getRGB()));
}
