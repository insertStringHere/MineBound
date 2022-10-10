package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.item.custom.FlarpItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MineBound.MOD_ID);

    public static final RegistryObject<Item> FLARP = ITEMS.register("flarp",
            () -> new FlarpItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
