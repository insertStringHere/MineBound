package com.mineboundteam.minebound.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class Registry {
    public static void RegisterMod(IEventBus iEventBus){
        BlockEntityRegistry.BLOCK_ENTITIES.register(iEventBus);
        BlockRegistry.BLOCKS.register(iEventBus);
        ContainerRegistry.CONTAINERS.register(iEventBus);
        RecipeRegistry.RECIPE_SERIALIZERS.register(iEventBus);
    }
}
