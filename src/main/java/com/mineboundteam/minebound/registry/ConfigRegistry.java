package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.config.ManaConfig;
import com.mineboundteam.minebound.registry.config.ArmorConfigRegistry;
import com.mineboundteam.minebound.registry.config.ServerConfigRegistry;

import net.minecraftforge.eventbus.api.IEventBus;

public class ConfigRegistry {
    public static void register(IEventBus eventBus){
        // Register server configs
        ServerConfigRegistry.get()
            .addConfig(new ManaConfig())
            .addConfig(new ArmorConfigRegistry())
            .register(eventBus);
    }
}
