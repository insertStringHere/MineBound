package com.mineboundteam.minebound.item.armor;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public enum ArmorTier {
    EFFIGY(0, 1), // Tier 1
    SUIT(1, 1), // Tier 2
    SYNERGY(2, 2), // Tier 3
    SINGULARITY(3, 2); // Tier 4

    private final int id;
    public final int handSlots;

    ArmorTier(int id, int handSlots) {
        this.id = id;
        this.handSlots = handSlots;
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
