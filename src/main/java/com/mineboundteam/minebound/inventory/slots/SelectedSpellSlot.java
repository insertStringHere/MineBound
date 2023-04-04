package com.mineboundteam.minebound.inventory.slots;

import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.inventory.containers.InventorySpellContainer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SelectedSpellSlot extends Slot {
    protected final InventorySpellContainer myContainer;

    public SelectedSpellSlot(InventorySpellContainer pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
        myContainer = pContainer;
    }

    @Override
    public boolean mayPickup(Player pPlayer){
        return myContainer.source.equals(ArmorNBTHelper.ACTIVE_SPELL);
    }

    @Override
    public boolean mayPlace(ItemStack item){
        return false; 
    }

    
}
