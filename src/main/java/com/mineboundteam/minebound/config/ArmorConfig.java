package com.mineboundteam.minebound.config;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ArmorConfig implements IConfig {

    public int MANAPOOL;
    public int RECOVERY;
    public int ENERGY;
    public int STORAGE_SLOTS;
    public int UTILITY_SLOTS;

    private final int defaultManapool;
    private final int defaultRecovery;
    private final int defaultEnergy;
    private final int defaultStorage;
    private final int defaultUtility;

    public final String name;

    public ArmorConfig(String name, int manapool, int recovery, int energy, int storageSlots, int utilitySlots){
        this.name = name;
        this.defaultManapool = manapool;
        this.defaultRecovery = recovery;
        this.defaultEnergy = energy;
        this.defaultStorage = storageSlots;
        this.defaultUtility = utilitySlots;
    }


    @Override
    public void build(Builder builder) {
        builder.push(name);
        MANAPOOL = builder.comment("Manapool increase").defineInRange("manapool_increase", defaultManapool, 0, 10000).get();
        RECOVERY = builder.comment("Recovery speed increase").defineInRange("recovery_increase", defaultRecovery, 0, 10000).get();
        ENERGY = builder.comment("Armor piece energy").defineInRange("armor_energy", defaultEnergy, 0, 10000).get();
        STORAGE_SLOTS = builder.comment("Storage slot count").defineInRange("storage_slots", defaultStorage, 0, 10000).get();
        UTILITY_SLOTS = builder.comment("Utility slot count").defineInRange("utility_slots", defaultUtility, 0, 10000).get();

        builder.pop();
        
    }

    @Override
    public void refresh(ModConfigEvent event) {      
    }
    
}
