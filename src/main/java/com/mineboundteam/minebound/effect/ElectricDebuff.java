package com.mineboundteam.minebound.effect;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.effect.registry.EffectRegistry;
import com.mineboundteam.minebound.util.MBDamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class ElectricDebuff extends MobEffect {
    public ElectricDebuff(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, getSlownessLevel(pAmplifier), false, false));
    }

    public static int getSlownessLevel(int amplifier) {
        return ((amplifier + 1) * 2) - 1;
    }

    public static double getDmgMult(int amplifier) {
        return 1d + ((amplifier + 1) * 0.25d);
    }

    @SubscribeEvent
    public static void triggerEffect(LivingDamageEvent event) {
        if (!event.getEntity().level.isClientSide() && event.getEntityLiving().hasEffect(EffectRegistry.ELECTRIC_DEBUFF.get())
                && event.getSource().getMsgId().equals(MBDamageSources.ELECTRICITY)) {
            MobEffectInstance effect = event.getEntityLiving().getEffect(EffectRegistry.ELECTRIC_DEBUFF.get());
            double amount = event.getAmount() * getDmgMult(effect.getAmplifier());
            event.setAmount((float) amount);
        }
    }
}
