package com.mineboundteam.minebound.capabilities;

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

public class PlayerFireUtilityToggleProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerFireUtilityToggleProvider.FireUtilityToggle> FIRE_UTILITY_TOGGLE = CapabilityManager.get(new CapabilityToken<>() { });

    private PlayerFireUtilityToggleProvider.FireUtilityToggle fireUtilityToggle = null;
    private final LazyOptional<PlayerFireUtilityToggleProvider.FireUtilityToggle> optional = LazyOptional.of(this::createFireUtilityToggle);


    private @NotNull PlayerFireUtilityToggleProvider.FireUtilityToggle createFireUtilityToggle() {
        if(this.fireUtilityToggle == null)
            this.fireUtilityToggle = new FireUtilityToggle().setFireUtilityIsActive(false);

        return this.fireUtilityToggle;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == FIRE_UTILITY_TOGGLE ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createFireUtilityToggle().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createFireUtilityToggle().loadNBTData(nbt);
    }

    public class FireUtilityToggle {
        private boolean fireUtilityIsActive = false;

        public boolean getFireUtilityIsActive() {
            return fireUtilityIsActive;
        }
        public FireUtilityToggle setFireUtilityIsActive(boolean fireUtilityIsActive) {
             this.fireUtilityIsActive = fireUtilityIsActive;
             return this;
        }

        public void copyFrom(PlayerFireUtilityToggleProvider.FireUtilityToggle source){
            this.fireUtilityIsActive = source.getFireUtilityIsActive();
        }

        public void saveNBTData(CompoundTag nbt){
            nbt.putBoolean("fireUtilityToggle", this.fireUtilityIsActive);
        }

        public void loadNBTData(CompoundTag nbt){
            fireUtilityIsActive = nbt.getBoolean("fireUtilityToggle");
        }

    }
}
