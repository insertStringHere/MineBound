package com.mineboundteam.minebound.effect;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class FireBarrierEffect extends MobEffect {
    public FireBarrierEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @SubscribeEvent
    public static void triggerFireBarrierEffect(LivingHurtEvent event) {
        if(!event.getEntity().level.isClientSide()) {
            if (event.getEntityLiving() instanceof Player player) {
                if(player.hasEffect(EffectRegistry.FIRE_BARRIER.get())) {
                    Entity sourceEntity = event.getSource().getEntity();
                    if(sourceEntity != null) {
                        sourceEntity.setSecondsOnFire(5);
                    }
                }
            }
        }
    }
}