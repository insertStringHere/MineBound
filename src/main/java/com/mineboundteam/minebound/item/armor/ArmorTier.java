package com.mineboundteam.minebound.item.armor;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public enum ArmorTier {
    EFFIGY(0), // Tier 1
    SUIT(1), // Tier 2
    SYNERGY(2), // Tier 3
    SINGULARITY(3); // Tier 4

    private final int id;

    ArmorTier(int id) {
        this.id = id;
    }

    public Component getLabel() {
        return new TranslatableComponent("armor." + MineBound.MOD_ID + "." + getValue());
    }

    @Override
    public String toString() {
        return "Tier " + (getValue() + 1);
    }

    public int getValue() {
        return this.id;
    }
}
