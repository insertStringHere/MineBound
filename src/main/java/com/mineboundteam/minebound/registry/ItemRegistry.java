package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MineBound.MOD_ID);

    public static final RegistryObject<Item> MYRI_CHUNK = ITEMS.register("myri_chunk", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRI_MANASAC = ITEMS.register("myri_manasac", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
}
