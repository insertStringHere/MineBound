package com.mineboundteam.minebound.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * An interface to handle creating and refreshing config files
 */
public interface IConfig {
    
    /**
     * Creates the initial registration of the configuration file
     * @param builder - The {@link ForgeConfigSpec.Builder} which is building the config.
     */
    public abstract void build(ForgeConfigSpec.Builder builder);

    /**
     * Will refresh any additional components that use the configuration
     * that might need updating.
     * @param event - The {@link ModConfigEvent} that called a refresh action.
     */
    public abstract void refresh(ModConfigEvent event);
}
