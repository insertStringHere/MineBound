package com.mineboundteam.minebound.capabilities;

import com.mineboundteam.minebound.config.ManaConfig;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerManaProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerMana> PLAYER_MANA = CapabilityManager.get(new CapabilityToken<>() { });

    private PlayerMana mana = null;
    private final LazyOptional<PlayerMana> optional = LazyOptional.of(this::createPlayerMana);

    private @NotNull PlayerMana createPlayerMana() {
        if(this.mana == null)
            this.mana = new PlayerMana().setMana(0).setManaRecRate(ManaConfig.manaRecovery.get());

        return this.mana;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_MANA ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerMana().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerMana().loadNBTData(nbt);
    }

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
}
