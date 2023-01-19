package com.mineboundteam.minebound.item.armor;

public enum ArmorTier {
    EFFIGY(0),
    SUIT(1),
    SYNERGY(2),
    SINGULARITY(3);

    private final int id;
    ArmorTier(int id){
        this.id = id;
    }

    @Override
    public String toString(){
        return this.name().toLowerCase();
    }

    public int getValue(){
        return this.id;
    }
}
