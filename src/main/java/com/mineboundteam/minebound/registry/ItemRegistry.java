package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.item.MyriCorpseItem;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MineBound.MOD_ID);

    public static final RegistryObject<Item> GILDED_DIAMOND_INGOT = ITEMS.register("gilded_diamond_ingot", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRIAL_DIAMOND_INGOT = ITEMS.register("myrial_diamond_ingot", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRIAL_STEEL_INGOT = ITEMS.register("myrial_steel_ingot", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRI_CHUNK = ITEMS.register("myri_chunk", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRI_MANASAC = ITEMS.register("myri_manasac", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB).food(new FoodProperties.Builder().meat().alwaysEat().effect(() -> new MobEffectInstance(MobEffects.WITHER, 25), 1f).build())));
    public static final RegistryObject<Item> MYRI_CORPSE = ITEMS.register("myri_corpse", () -> new MyriCorpseItem(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
}
