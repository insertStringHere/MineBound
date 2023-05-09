package com.mineboundteam.minebound.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;

public class MBDamageSources {
    public static final String ELECTRICITY = mbSource("electricity");

    public static DamageSource electrified(LivingEntity source) {
        return new EntityDamageSource(ELECTRICITY, source);
    }

    public static String mbSource(String name) {
        return "minebound." + name;
    }
}
