package com.mineboundteam.minebound.capabilities;

import com.mineboundteam.minebound.config.ManaConfig;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class PlayerManaProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerMana> PLAYER_MANA = CapabilityManager.get(new CapabilityToken<>() { });

    private PlayerMana mana = null;
    private final LazyOptional<PlayerMana> optional = LazyOptional.of(this::createPlayerMana);

    private @NotNull PlayerMana createPlayerMana() {
        if(this.mana == null)
            this.mana = new PlayerMana().setMana(0);

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
        private int totalManaCap;

        private int availableManaCap;

        private final HashMap<String, Integer> manaCapModifiers = new HashMap<>();
        private final int manaRecRate = ManaConfig.manaRecovery.get();
        private final int baseManaCap = ManaConfig.baseManaCap.get();

        public PlayerMana setMana(int amt){
            mana = amt;
            return this;
        }

        @OnlyIn(Dist.CLIENT)
        public void setAvailableManaCap(int amt){
            availableManaCap = amt;
        }

        public void setTotalManaCap(int amt) {
            totalManaCap = availableManaCap = amt;
        }

        public void setManaCapModifier(String key, Integer value) {
            this.manaCapModifiers.put(key, value);
        }

        public int getMana(){
            return mana;
        }

        public int getManaRecRate(){
            return manaRecRate;
        }

        /**
         * Get mana useable by the player; may be more or
         * less than total mana cap.
         * @return Usable mana cap
         */
        public int getAvailableManaCap() {
           
            int available = availableManaCap;
            for (Integer modifier : manaCapModifiers.values())
                available += modifier;
            return available; 
        
        }

        /**
         * The total mana cap of the player, based on
         * equipped armor and baubles. If available mana cap
         * is larger, will use that
         * @return total mana cap
         */
        public int getTotalManaCap() {
            return Math.max(totalManaCap, getAvailableManaCap());
        }

        public int getBaseManaCap(){
            return baseManaCap;
        }

        public HashMap<String, Integer> getManaCapModifiers() {
            return manaCapModifiers;
        }

        public void addMana(int amt){
            this.mana = Math.min(mana + amt, getAvailableManaCap());
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
