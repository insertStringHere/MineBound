package com.mineboundteam.minebound.config;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * Configuration concerning player mana
 */
public class ManaConfig implements IConfig{

    public static IntValue baseManaCap;
    public static IntValue manaRecovery;

    public static BooleanValue keepArmor;

    /**
     * Sets up the section of the config file to manage mana values
     */
    @Override
    public void build(Builder builder) {
        builder.push("Mana Management");
        
        baseManaCap = builder.comment("The maximum amount of mana a player has without any augmentation. [Default = 200]").defineInRange("Max_Mana", 200, 0, 10000);
        manaRecovery = builder.comment("The recovery rate of mana per second by the player without augmentation. [Default = 1]").defineInRange("Mana_Recovery", 1, 0, 10000);

        keepArmor = builder.comment("Whether or not Myrial Armor should remain on the player after dying.").define("keep_armor", true);

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
