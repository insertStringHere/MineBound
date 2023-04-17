package com.mineboundteam.minebound.inventory.slots;

import com.mineboundteam.minebound.inventory.ArmorForgeMenu;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InputArmorSlot extends PlayerArmorSlot {
    protected ArmorForgeMenu menu;

    public InputArmorSlot(ArmorForgeMenu menu, Container container, int slotIndex, int x, int y) {
        super(EquipmentSlot.CHEST, menu.player, container, slotIndex, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack input) {
        return input.getItem() instanceof MyrialArmorItem;
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }

    @Override
    public void set(ItemStack pStack){
        super.set(pStack);
        if(pStack.getItem() instanceof MyrialArmorItem item){
            menu.activeSpells.armorCount = item.getConfig().STORAGE_SLOTS.get();
            menu.passiveSpells.armorCount = item.getConfig().UTILITY_SLOTS.get();
        }
    }

    @Override
    public ItemStack remove(int pAmount){
        menu.activeSpells.armorCount = menu.passiveSpells.armorCount = 0;
        return super.remove(pAmount);
    }

    public MyrialArmorItem getArmor(){
        if(this.getItem().isEmpty())
            return null;
        return (MyrialArmorItem)this.getItem().getItem();
    }
}