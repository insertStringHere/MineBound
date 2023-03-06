package com.mineboundteam.minebound.config.registry;

import com.mineboundteam.minebound.config.ManaConfig;

import net.minecraftforge.eventbus.api.IEventBus;

public class ConfigRegistry {
    public static void register(IEventBus eventBus){
        // Register server configs
        ServerConfigRegistry.get()
            .addConfig(new ManaConfig())
            .register(eventBus);
        
        MagicConfigRegistry.get().register(eventBus);
        ArmorConfigRegistry.get().register(eventBus);
    }
}
