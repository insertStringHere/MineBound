package com.mineboundteam.minebound.container;
import net.minecraftforge.energy.EnergyStorage;

public abstract class MineboundEnergyStorage extends EnergyStorage {
    public MineboundEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if (extractedEnergy != 0) {
            onEnergyChanged();
        }
        return extractedEnergy;
    }

    public abstract void onEnergyChanged();

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if (receiveEnergy != 0) {
            onEnergyChanged();
        }
        return receiveEnergy;
    }

    public int setEnergy(int energy) {
        this.energy = energy;
        return energy;
    }
}
