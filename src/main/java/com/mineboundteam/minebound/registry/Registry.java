package com.mineboundteam.minebound.registry;


import net.minecraftforge.eventbus.api.IEventBus;

public class Registry {
    public static void RegisterMod(IEventBus iEventBus){
        BlockRegistry.BLOCKS.register(iEventBus);
        ItemRegistry.ITEMS.register(iEventBus);
        StructureRegistry.STRUCTURES.register(iEventBus);
        KeyRegistry.register();
    }
}