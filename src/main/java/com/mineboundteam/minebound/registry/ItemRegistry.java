
package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;

import com.mineboundteam.minebound.item.Jello;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MineBound.MOD_ID);

    public static final RegistryObject<Item> JELLO = ITEMS.register("jello", () -> new Jello(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
}
