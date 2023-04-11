package com.mineboundteam.minebound.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerUtilityToggleProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<UtilityToggle> UTILITY_TOGGLE = CapabilityManager.get(new CapabilityToken<>() { });

    private UtilityToggle utilityToggle = null;
    private final LazyOptional<UtilityToggle> optional = LazyOptional.of(this::createUtilityToggle);


    private @NotNull PlayerUtilityToggleProvider.UtilityToggle createUtilityToggle() {
        if(this.utilityToggle == null) {
            this.utilityToggle = new UtilityToggle();
        }
        return this.utilityToggle;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == UTILITY_TOGGLE ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createUtilityToggle().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createUtilityToggle().loadNBTData(nbt);
    }

    public static void UpdatePlayerSync(Player oldPlayer, Player newPlayer){
        oldPlayer.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(oldStore ->
                newPlayer.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(newStore ->
                        newStore.copyFrom(oldStore)
                )
        );
    }
    public static class UtilityToggle {
        public boolean fire = false;
        public boolean earth = false;

        public void copyFrom(UtilityToggle source){
            this.fire = source.fire;
            this.earth = source.earth;
        }

        public void saveNBTData(CompoundTag nbt){
            nbt.putBoolean("fireUtilityToggle", this.fire);
            nbt.putBoolean("earthUtilityToggle", this.earth);
        }

        public void loadNBTData(CompoundTag nbt){
            fire = nbt.getBoolean("fireUtilityToggle");
            earth = nbt.getBoolean("earthUtilityToggle");
        }

    }
}
