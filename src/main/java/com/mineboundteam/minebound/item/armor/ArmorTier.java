package com.mineboundteam.minebound.item.armor;

import com.mineboundteam.minebound.MineBound;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public enum ArmorTier {
    EFFIGY(0),
    SUIT(1),
    SYNERGY(2),
    SINGULARITY(3);

    private final int id;
    ArmorTier(int id){
        this.id = id;
    }

    public Component getLabel(){
        return new TranslatableComponent("armor." + MineBound.MOD_ID + "." + getValue());
    }

    @Override
    public String toString(){
        return getLabel().getContents();
    }

    public int getValue(){
        return this.id;
    }
}
