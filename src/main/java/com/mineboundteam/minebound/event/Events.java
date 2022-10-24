package com.mineboundteam.minebound.event;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class Events {

    @SubscribeEvent
    public static void cancelDamage(LivingAttackEvent event) {
        if(!event.getEntity().level.isClientSide()) {
            if(event.getEntityLiving() instanceof Player player) {
                // Cancel the damage event if wearing diamond helmet
                // We listen for LivingAttackEvent to prevent the damage taken animation/sound from playing
                if(player.getItemBySlot(EquipmentSlot.HEAD).getItem().equals(Item.byId(750))) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void reduceDamage(LivingHurtEvent event) {
        if(!event.getEntity().level.isClientSide()) {
            if(event.getEntityLiving() instanceof Player player) {
                // Reduce damage by half if wearing diamond chestplate
                if(player.getItemBySlot(EquipmentSlot.CHEST).getItem().equals(Item.byId(751))) {
                    event.setAmount(event.getAmount() * 0.5f);
                }
            }
        }
    }

    @SubscribeEvent
    public static void reflectDamage(LivingHurtEvent event) {
        if(!event.getEntity().level.isClientSide()) {
            if(event.getEntityLiving() instanceof Player player) {
                Entity sourceEntity = event.getSource().getEntity();
                // Reflect the damage back onto the attacker using the thorns damage source. Only if wearing diamond leggings
                if(sourceEntity != null && player.getItemBySlot(EquipmentSlot.LEGS).getItem().equals(Item.byId(752))) {
                    sourceEntity.hurt(DamageSource.thorns(player), event.getAmount() / 2f);
                }
            }
        }
    }
}
