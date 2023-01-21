package com.mineboundteam.minebound.mana;

import com.mineboundteam.minebound.config.ManaConfig;
import net.minecraft.nbt.CompoundTag;

public class PlayerMana {
    private int mana;
    private int manaRecRate;
    private int manaCap;
    private final int manaMax = ManaConfig.maximumMana.get();

    public PlayerMana setMana(int amt){
        mana = amt;
        return this;
    }

    public PlayerMana setManaRecRate(int amt){
        manaRecRate = amt;
        return this;
    }

    public void setManaCap(int amt) {
        manaCap = amt;
    }

    public int getMana(){
        return mana;
    }

    public int getManaRecRate(){
        return manaRecRate;
    }

    public int getManaCap() {
        return manaCap;
    }

    public int getManaMax(){
        return manaMax;
    }

    public void addMana(int amt){
        this.mana = Math.min(mana + amt, this.manaCap);
    }

    public void subtractMana(int amt){
        this.mana = Math.max(mana - amt, 0);
    }

    public void copyFrom(PlayerMana source){
        this.mana = source.getMana();
    }

    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("mana", this.mana);
    }

    public void loadNBTData(CompoundTag nbt){
        mana = nbt.getInt("mana");
    }
}
