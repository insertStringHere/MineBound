package com.mineboundteam.minebound.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class Registry {
    public static void RegisterMod(IEventBus iEventBus){
        BlockRegistry.BLOCKS.register(iEventBus);      
        ItemRegistry.ITEMS.register(iEventBus);
        EntityRegistry.ENTITIES.register(iEventBus);
        
        ConfigRegistry.register(iEventBus);
    }
}
