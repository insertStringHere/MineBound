package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.effect.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void triggerFireBarrierEffect(LivingHurtEvent event) {
        if(!event.getEntity().level.isClientSide()) {
            if (event.getEntityLiving() instanceof Player player) {
                if(player.hasEffect(ModEffects.FIRE_BARRIER.get())) {
                    Entity sourceEntity = event.getSource().getEntity();
                    if(sourceEntity != null) {
                        sourceEntity.setSecondsOnFire(5);
                    }
                }
            }
        }
    }
}
