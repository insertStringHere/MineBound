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

public class ArmorFireUtilityToggleProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ArmorFireUtilityToggleProvider.ArmorFireUtilityToggle> ARMOR_FIRE_UTILITY_TOGGLE = CapabilityManager.get(new CapabilityToken<>() { });

    protected final LazyOptional<ArmorFireUtilityToggle> optional = LazyOptional.of(this::createArmorFireUtilityToggle);

    protected ArmorFireUtilityToggle armorFireUtilityToggle;

    private ArmorFireUtilityToggle createArmorFireUtilityToggle() {
        if(this.armorFireUtilityToggle == null)
            this.armorFireUtilityToggle = new ArmorFireUtilityToggle();

        return this.armorFireUtilityToggle;
    }
    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ARMOR_FIRE_UTILITY_TOGGLE ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createArmorFireUtilityToggle().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createArmorFireUtilityToggle().loadNBTData(nbt);
    }


    public static class ArmorFireUtilityToggle{
        public boolean armorFireUtilityIsActive = false;

        public boolean getArmorFireUtilityIsActive() {
            return armorFireUtilityIsActive;
        }
        public void setArmorFireUtilityIsActive(boolean armorFireUtilityIsActive) {
            this.armorFireUtilityIsActive = armorFireUtilityIsActive;
        }
        public void saveNBTData(CompoundTag compound) {
            compound.putBoolean("armorFireUtilityIsActive", armorFireUtilityIsActive);
        }
        public void loadNBTData(CompoundTag compound) {
            armorFireUtilityIsActive = compound.getBoolean("armorFireUtilityIsActive");
        }

    }
}
