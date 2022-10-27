package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MineBound.MOD_ID);
    public static final RegistryObject<Item> CHEESE_SANDWICH = ITEMS.register("cheese_sandwich", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(3).build()).tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> CHEESE_WEDGE = ITEMS.register("cheese_wedge", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(2).build()).tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> FLARP = ITEMS.register("flarp", () -> new Flarp(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> GRILLED_CHEESE = ITEMS.register("grilled_cheese", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(15).build()).tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> JELLO = ITEMS.register("jello", () -> new SwordItem(Tiers.DIAMOND, 11, 2f, new Item.Properties().tab(MineBound.MINEBOUND_TAB).food(new FoodProperties.Builder().nutrition(8).saturationMod(14.4f).alwaysEat().effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 0.5F).build())));
}
