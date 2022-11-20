package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class Jello extends Item implements ICurioItem {
    public Jello(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if (!player.level.isClientSide()) {
            // Add fire barrier effect that will not expire
            player.addEffect(new MobEffectInstance(EffectRegistry.FIRE_BARRIER.get(), 99999));
        }

        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if (!player.level.isClientSide()) {
            // Remove the fire barrier effect
            player.removeEffect(EffectRegistry.FIRE_BARRIER.get());
        }

        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }
}
