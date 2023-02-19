package com.mineboundteam.minebound.config;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * Configuration concerning player mana
 */
public class ManaConfig implements IConfig{

    public static IntValue maximumMana;
    public static IntValue manaRecovery;

    /**
     * Sets up the section of the config file to manage mana values
     */
    @Override
    public void build(Builder builder) {
        builder.push("Mana Management");
        
        maximumMana = builder.comment("The maximum amount of mana a player has without any augmentation. [Default = 200]").defineInRange("Max_Mana", 200, 0, 10000);  
        manaRecovery = builder.comment("The recovery rate of mana per second by the player without augmentation. [Default = 1]").defineInRange("Mana_Recovery", 1, 0, 10000);

        builder.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh(ModConfigEvent event) {
        // Any additional work that needs done 
    }
    
}
