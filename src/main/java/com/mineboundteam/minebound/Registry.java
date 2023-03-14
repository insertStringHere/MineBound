package com.mineboundteam.minebound;

import com.mineboundteam.minebound.block.registry.BlockRegistry;
import com.mineboundteam.minebound.config.registry.ConfigRegistry;
import com.mineboundteam.minebound.entity.registry.EntityRegistry;
import com.mineboundteam.minebound.inventory.registry.MenuRegistry;
import com.mineboundteam.minebound.inventory.registry.RecipeRegistry;
import com.mineboundteam.minebound.item.registry.ItemRegistry;

import net.minecraftforge.eventbus.api.IEventBus;

public class Registry {
    public static void RegisterMod(IEventBus iEventBus){
        ItemRegistry.ITEMS.register(iEventBus);
        BlockRegistry.BLOCKS.register(iEventBus);
        BlockRegistry.BLOCK_ENTITIES.register(iEventBus);

        ConfigRegistry.register(iEventBus);
        
        MenuRegistry.MENUS.register(iEventBus);
        EntityRegistry.ENTITIES.register(iEventBus);

        RecipeRegistry.RECIPE_SERIALIZERS.register(iEventBus);
        RecipeRegistry.RECIPE_TYPE.register(iEventBus);
    }
}
