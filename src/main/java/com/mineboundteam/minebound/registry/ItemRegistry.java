package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.ArmorConfig;
import com.mineboundteam.minebound.item.ArmorMaterials;
import com.mineboundteam.minebound.item.MyriCorpseItem;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.registry.config.ArmorConfigRegistry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MineBound.MOD_ID);

    public static final RegistryObject<Item> ENERGIZED_IRON_INGOT = ITEMS.register("energized_iron_ingot", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> GILDED_DIAMOND_INGOT = ITEMS.register("gilded_diamond_ingot", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ILLUMINANT_STEEL_INGOT = ITEMS.register("illuminant_steel_ingot", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRIAL_DIAMOND_INGOT = ITEMS.register("myrial_diamond_ingot", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRIAL_STEEL_INGOT = ITEMS.register("myrial_steel_ingot", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));


    public static final RegistryObject<Item> ALIEN_CIRCUITRY = ITEMS.register("alien_circuitry", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ENERGIZED_FOCUS = ITEMS.register("energized_focus", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> GILDED_DIAMOND_WIRE = ITEMS.register("gilded_diamond_wire", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    
    public static final RegistryObject<Item> MYRI_CHUNK = ITEMS.register("myri_chunk", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRI_MANASAC = ITEMS.register("myri_manasac", () -> new Item(new Item.Properties().tab(MineBound.MINEBOUND_TAB).food(new FoodProperties.Builder().meat().alwaysEat().effect(() -> new MobEffectInstance(MobEffects.WITHER, 250, 4), 1f).build())));
    public static final RegistryObject<Item> MYRI_CORPSE = ITEMS.register("myri_corpse", () -> new MyriCorpseItem(new Item.Properties().tab(MineBound.MINEBOUND_TAB)));

    public static final RegistryObject<Item> MYRIAL_EFFIGY_HELMET = ITEMS.register("myrial_effigy_helmet", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SUIT, EquipmentSlot.HEAD, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.EFFIGY, ArmorConfigRegistry.EFFIGY_HELMET));
    public static final RegistryObject<Item> MYRIAL_EFFIGY_CHESTPLATE = ITEMS.register("myrial_effigy_chestplate", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SUIT, EquipmentSlot.CHEST, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.EFFIGY, ArmorConfigRegistry.EFFIGY_CHESTPLATE));
    public static final RegistryObject<Item> MYRIAL_EFFIGY_LEGGINGS = ITEMS.register("myrial_effigy_leggings", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SUIT, EquipmentSlot.LEGS, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.EFFIGY, ArmorConfigRegistry.EFFIGY_LEGGINGS));
    public static final RegistryObject<Item> MYRIAL_EFFIGY_BOOTS = ITEMS.register("myrial_effigy_boots", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SUIT, EquipmentSlot.FEET, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.EFFIGY, ArmorConfigRegistry.EFFIGY_BOOTS));

}
