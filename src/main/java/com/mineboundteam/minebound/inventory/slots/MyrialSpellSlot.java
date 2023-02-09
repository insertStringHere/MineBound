package com.mineboundteam.minebound.inventory.slots;

import com.mineboundteam.minebound.config.ArmorConfig;
import com.mineboundteam.minebound.inventory.containers.ArmorSpellContainer;
import com.mineboundteam.minebound.magic.ActiveSpellItem;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class MyrialSpellSlot extends Slot {
    protected InputArmorSlot armorSlot;


    public MyrialSpellSlot(ArmorSpellContainer armorSlot, int pSlot, int pX, int pY) {
        super(armorSlot, pSlot, pX, pY);
        this.armorSlot = armorSlot.item;
    }

    @Override
    public boolean isActive(){
        return armorSlot.hasItem() && this.getSlotIndex() < getMaxSlots(armorSlot.getArmor().getConfig());
    }

    @Override
    public boolean mayPlace(ItemStack item){
        return item.getItem() instanceof ActiveSpellItem;
    }    
    
    protected abstract int getMaxSlots(ArmorConfig config); 

    public static class Active extends MyrialSpellSlot{
        public Active(ArmorSpellContainer armorSlot, int pSlot, int pX, int pY) {
            super(armorSlot, pSlot, pX, pY);
        }
        @Override
        protected int getMaxSlots(ArmorConfig config) {
            return config.STORAGE_SLOTS.get();
        }
    }

    public static class Passive extends MyrialSpellSlot{
        public Passive(ArmorSpellContainer armorSlot, int pSlot, int pX, int pY) {
            super(armorSlot, pSlot, pX, pY);
        }
        @Override
        protected int getMaxSlots(ArmorConfig config) {
            return config.UTILITY_SLOTS.get();
        }
    }
}
