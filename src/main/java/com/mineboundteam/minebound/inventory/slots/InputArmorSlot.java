package com.mineboundteam.minebound.inventory.slots;

import com.mineboundteam.minebound.item.armor.MyrialArmorItem;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InputArmorSlot extends PlayerArmorSlot {
    public InputArmorSlot(Player player, Container container, int slotIndex, int x, int y) {
        super(EquipmentSlot.CHEST, player, container, slotIndex, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack input) {
        return input.getItem() instanceof MyrialArmorItem;
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }

    public MyrialArmorItem getArmor(){
        if(this.getItem().isEmpty())
            return null;
        return (MyrialArmorItem)this.getItem().getItem();
    }
}