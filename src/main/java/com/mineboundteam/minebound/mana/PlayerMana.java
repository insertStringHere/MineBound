package com.mineboundteam.minebound.mana;

import com.mineboundteam.minebound.config.ManaConfig;
import net.minecraft.nbt.CompoundTag;

public class PlayerMana {
    private int mana;
    private int manaRecRate;
    private int availableManaCap;
    private int totalManaCap;
    private final int manaMax = ManaConfig.maximumMana.get();

    public PlayerMana setMana(int amt){
        mana = amt;
        return this;
    }

    public PlayerMana setManaRecRate(int amt){
        manaRecRate = amt;
        return this;
    }

    public void setAvailableManaCap(int amt) {
        availableManaCap = amt;
    }

    public void setTotalManaCap(int amt) {
        totalManaCap = amt;
    }

    public int getMana(){
        return mana;
    }

    public int getManaRecRate(){
        return manaRecRate;
    }

    public int getAvailableManaCap() {
        return availableManaCap;
    }

    public int getTotalManaCap() {
        return totalManaCap;
    }

    public int getManaMax(){
        return manaMax;
    }

    public void addMana(int amt){
        this.mana = Math.min(mana + amt, this.availableManaCap);
    }

    public int subtractMana(int amt){
        int underflow = Math.max(amt - this.mana, 0);
        this.mana = Math.max(mana - amt, 0);
        return underflow;
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
