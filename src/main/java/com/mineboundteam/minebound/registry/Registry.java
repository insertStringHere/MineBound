package com.mineboundteam.minebound.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class Registry {
    public static void RegisterMod(IEventBus iEventBus){
        ItemRegistry.ITEMS.register(iEventBus);
        BlockRegistry.BLOCKS.register(iEventBus);
        BlockRegistry.BLOCK_ENTITIES.register(iEventBus);
        ParticleRegistry.PARTICLES.register(iEventBus);

        ConfigRegistry.register(iEventBus);
        
        MenuRegistry.MENUS.register(iEventBus);
        EntityRegistry.ENTITIES.register(iEventBus);

        RecipeRegistry.RECIPE_SERIALIZERS.register(iEventBus);
        RecipeRegistry.RECIPE_TYPE.register(iEventBus);
    }
}
