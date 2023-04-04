package com.mineboundteam.minebound.inventory.containers;

import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.inventory.ArmorForgeMenu;
import com.mineboundteam.minebound.inventory.slots.InputArmorSlot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ArmorSpellContainer implements Container {
    public final InputArmorSlot item;
    public int armorCount; 
    private String spellTag;


    public ArmorSpellContainer(ArmorForgeMenu menu, String spellTag) {
        this.item = (InputArmorSlot)menu.slots.get(ArmorForgeMenu.ARMOR_INPUT_INDEX);
        this.spellTag = spellTag;
    }

    @Override
    public void clearContent() {
        ArmorNBTHelper.getSpellTag(item.getItem(), spellTag).clear();
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        return ArmorNBTHelper.getSpellTag(item.getItem(), spellTag).isEmpty();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return ItemStack.of(ArmorNBTHelper.getSpellTag(item.getItem(), spellTag).getCompound(pSlot));
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ItemStack.of((CompoundTag)ArmorNBTHelper.getSpellTag(item.getItem(), spellTag).remove(pSlot));
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ItemStack.of((CompoundTag)ArmorNBTHelper.getSpellTag(item.getItem(), spellTag).remove(pSlot));
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if(pSlot >= armorCount)
            return;

        var list = ArmorNBTHelper.getSpellTag(item.getItem(), spellTag);
        for(int i = list.size()-1; i < pSlot; i++)
            list.add(ItemStack.EMPTY.serializeNBT());
        
        list.setTag(pSlot, pStack.serializeNBT());
        ArmorNBTHelper.saveSpellTag(item.getItem(), spellTag, list);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void setChanged() {}

    public boolean isActive() {
        return item.hasItem() && armorCount > 0;
    }
}