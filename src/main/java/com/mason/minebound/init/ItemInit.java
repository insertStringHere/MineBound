package com.mason.minebound.init;

import com.google.common.base.Supplier;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.mason.minebound.MineBound.MINEBOUND_TAB;
import static com.mason.minebound.MineBound.MOD_ID;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> JELLO_ITEM = register("jello_item", () -> new Item(new Item.Properties().tab(MINEBOUND_TAB)));

    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
}
