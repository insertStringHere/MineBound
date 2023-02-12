package com.mineboundteam.minebound.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ArmorRecoveryProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>{
    public static Capability<ArmorRecovery> ARMOR_RECOVERY = CapabilityManager.get(new CapabilityToken<>() { });

    private ArmorRecovery recovery;
    private final LazyOptional<ArmorRecovery> optional = LazyOptional.of(this::createRecovery);

    private ArmorRecovery createRecovery() {
        if(this.recovery == null)
            this.recovery = new ArmorRecovery();

        return this.recovery;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag t = new CompoundTag(); 
        createRecovery().saveNBT(t);
        return t; 
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createRecovery().loadNBTData(nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ARMOR_RECOVERY ? optional.cast() : LazyOptional.empty();
    }

    public static class ArmorRecovery {
        public boolean recovering;

        public void saveNBT(CompoundTag nbt) {
            nbt.putBoolean("recovering", recovering);
        }

        public void loadNBTData(CompoundTag nbt) {
            recovering = nbt.getBoolean("recovering");
        }
    }
    
}
