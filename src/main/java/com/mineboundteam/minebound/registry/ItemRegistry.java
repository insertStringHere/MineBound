package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.item.ArmorMaterials;
import com.mineboundteam.minebound.item.MyriCorpseItem;
import com.mineboundteam.minebound.item.ToolTier;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.OffensiveSpells.TestSpell;
import com.mineboundteam.minebound.magic.UtilitySpells.ShieldUtilitySpell;
import com.mineboundteam.minebound.magic.UtilitySpells.TelekineticUtilitySpell;
import com.mineboundteam.minebound.registry.config.ArmorConfigRegistry;
import com.mineboundteam.minebound.registry.config.MagicConfigRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
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

    public static final RegistryObject<Item> ENERGIZED_IRON_AXE = ITEMS.register("energized_iron_axe", () -> new AxeItem(ToolTier.ENERGIZED_IRON, 6, -3, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ENERGIZED_IRON_HOE = ITEMS.register("energized_iron_hoe", () -> new HoeItem(ToolTier.ENERGIZED_IRON, -2, -1, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ENERGIZED_IRON_PICKAXE = ITEMS.register("energized_iron_pickaxe", () -> new PickaxeItem(ToolTier.ENERGIZED_IRON, 1, -2.8F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ENERGIZED_IRON_SHOVEL = ITEMS.register("energized_iron_shovel", () -> new ShovelItem(ToolTier.ENERGIZED_IRON, 1.5F, -3, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ENERGIZED_IRON_SWORD = ITEMS.register("energized_iron_sword", () -> new SwordItem(ToolTier.ENERGIZED_IRON, 3, -2.4F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ILLUMINANT_STEEL_AXE = ITEMS.register("illuminant_steel_axe", () -> new AxeItem(ToolTier.ILLUMINANT_STEEL, 5, -3, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ILLUMINANT_STEEL_HOE = ITEMS.register("illuminant_steel_hoe", () -> new HoeItem(ToolTier.ILLUMINANT_STEEL, -3, 0, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ILLUMINANT_STEEL_PICKAXE = ITEMS.register("illuminant_steel_pickaxe", () -> new PickaxeItem(ToolTier.ILLUMINANT_STEEL, 1, -2.8F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ILLUMINANT_STEEL_SHOVEL = ITEMS.register("illuminant_steel_shovel", () -> new ShovelItem(ToolTier.ILLUMINANT_STEEL, 1.5F, -3, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ILLUMINANT_STEEL_SWORD = ITEMS.register("illuminant_steel_sword", () -> new SwordItem(ToolTier.ILLUMINANT_STEEL, 3, -2.4F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> GILDED_DIAMOND_AXE = ITEMS.register("gilded_diamond_axe", () -> new AxeItem(ToolTier.GILDED_DIAMOND, 5.0F, -3.0F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> GILDED_DIAMOND_HOE = ITEMS.register("gilded_diamond_hoe", () -> new HoeItem(ToolTier.GILDED_DIAMOND, -3, 0.0F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> GILDED_DIAMOND_PICKAXE = ITEMS.register("gilded_diamond_pickaxe", () -> new PickaxeItem(ToolTier.GILDED_DIAMOND, 1, -2.8F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> GILDED_DIAMOND_SHOVEL = ITEMS.register("gilded_diamond_shovel", () -> new ShovelItem(ToolTier.GILDED_DIAMOND, 1.5F, -3.0F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> GILDED_DIAMOND_SWORD = ITEMS.register("gilded_diamond_sword", () -> new SwordItem(ToolTier.GILDED_DIAMOND, 3, -2.4F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRIAL_DIAMOND_AXE = ITEMS.register("myrial_diamond_axe", () -> new AxeItem(ToolTier.MYRIAL_DIAMOND, 4.5F, -2.5F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRIAL_DIAMOND_HOE = ITEMS.register("myrial_diamond_hoe", () -> new HoeItem(ToolTier.MYRIAL_DIAMOND, -3, 0, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRIAL_DIAMOND_PICKAXE = ITEMS.register("myrial_diamond_pickaxe", () -> new PickaxeItem(ToolTier.MYRIAL_DIAMOND, 1, -2.8F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRIAL_DIAMOND_SHOVEL = ITEMS.register("myrial_diamond_shovel", () -> new ShovelItem(ToolTier.MYRIAL_DIAMOND, 1.5F, -2.5F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> MYRIAL_DIAMOND_SWORD = ITEMS.register("myrial_diamond_sword", () -> new SwordItem(ToolTier.MYRIAL_DIAMOND, 3, -2.4F, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    
    public static final RegistryObject<Item> MYRIAL_EFFIGY_HELMET = ITEMS.register("myrial_effigy_helmet", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_EFFIGY, EquipmentSlot.HEAD, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.EFFIGY, ArmorConfigRegistry.EFFIGY_HELMET));
    public static final RegistryObject<Item> MYRIAL_EFFIGY_CHESTPLATE = ITEMS.register("myrial_effigy_chestplate", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_EFFIGY, EquipmentSlot.CHEST, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.EFFIGY, ArmorConfigRegistry.EFFIGY_CHESTPLATE));
    public static final RegistryObject<Item> MYRIAL_EFFIGY_LEGGINGS = ITEMS.register("myrial_effigy_leggings", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_EFFIGY, EquipmentSlot.LEGS, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.EFFIGY, ArmorConfigRegistry.EFFIGY_LEGGINGS));
    public static final RegistryObject<Item> MYRIAL_EFFIGY_BOOTS = ITEMS.register("myrial_effigy_boots", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_EFFIGY, EquipmentSlot.FEET, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.EFFIGY, ArmorConfigRegistry.EFFIGY_BOOTS));
    
    public static final RegistryObject<Item> MYRIAL_SUIT_HELMET = ITEMS.register("myrial_suit_helmet", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SUIT, EquipmentSlot.HEAD, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.SUIT, ArmorConfigRegistry.SUIT_HELMET));
    public static final RegistryObject<Item> MYRIAL_SUIT_CHESTPLATE = ITEMS.register("myrial_suit_chestplate", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SUIT, EquipmentSlot.CHEST, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.SUIT, ArmorConfigRegistry.SUIT_CHESTPLATE));
    public static final RegistryObject<Item> MYRIAL_SUIT_LEGGINGS = ITEMS.register("myrial_suit_leggings", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SUIT, EquipmentSlot.LEGS, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.SUIT, ArmorConfigRegistry.SUIT_LEGGINGS));
    public static final RegistryObject<Item> MYRIAL_SUIT_BOOTS = ITEMS.register("myrial_suit_boots", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SUIT, EquipmentSlot.FEET, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.SUIT, ArmorConfigRegistry.SUIT_BOOTS));
    
    public static final RegistryObject<Item> MYRIAL_SYNERGY_HELMET = ITEMS.register("myrial_synergy_helmet", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SYNERGY, EquipmentSlot.HEAD, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.SYNERGY, ArmorConfigRegistry.SYNERGY_HELMET));
    public static final RegistryObject<Item> MYRIAL_SYNERGY_CHESTPLATE = ITEMS.register("myrial_synergy_chestplate", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SYNERGY, EquipmentSlot.CHEST, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.SYNERGY, ArmorConfigRegistry.SYNERGY_CHESTPLATE));
    public static final RegistryObject<Item> MYRIAL_SYNERGY_LEGGINGS = ITEMS.register("myrial_synergy_leggings", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SYNERGY, EquipmentSlot.LEGS, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.SYNERGY, ArmorConfigRegistry.SYNERGY_LEGGINGS));
    public static final RegistryObject<Item> MYRIAL_SYNERGY_BOOTS = ITEMS.register("myrial_synergy_boots", () -> new MyrialArmorItem(ArmorMaterials.MYRIAL_SYNERGY, EquipmentSlot.FEET, new Item.Properties().tab(MineBound.MINEBOUND_TAB), ArmorTier.SYNERGY, ArmorConfigRegistry.SYNERGY_BOOTS));

    public static final RegistryObject<Item> ENERGIZED_IRON_HELMET = ITEMS.register("energized_iron_helmet", () -> new ArmorItem(ArmorMaterials.ENERGIZED_IRON, EquipmentSlot.HEAD, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ENERGIZED_IRON_CHESTPLATE= ITEMS.register("energized_iron_chestplate", () -> new ArmorItem(ArmorMaterials.ENERGIZED_IRON, EquipmentSlot.CHEST, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ENERGIZED_IRON_LEGGINGS = ITEMS.register("energized_iron_leggings", () -> new ArmorItem(ArmorMaterials.ENERGIZED_IRON, EquipmentSlot.LEGS, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ENERGIZED_IRON_BOOTS = ITEMS.register("energized_iron_boots", () -> new ArmorItem(ArmorMaterials.ENERGIZED_IRON, EquipmentSlot.FEET, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    
    public static final RegistryObject<Item> ILLUMINANT_STEEL_HELMET = ITEMS.register("illuminant_steel_helmet", () -> new ArmorItem(ArmorMaterials.ILLUMINANT_STEEL, EquipmentSlot.HEAD, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ILLUMINANT_STEEL_CHESTPLATE= ITEMS.register("illuminant_steel_chestplate", () -> new ArmorItem(ArmorMaterials.ILLUMINANT_STEEL, EquipmentSlot.CHEST, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ILLUMINANT_STEEL_LEGGINGS = ITEMS.register("illuminant_steel_leggings", () -> new ArmorItem(ArmorMaterials.ILLUMINANT_STEEL, EquipmentSlot.LEGS, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));
    public static final RegistryObject<Item> ILLUMINANT_STEEL_BOOTS = ITEMS.register("illuminant_steel_boots", () -> new ArmorItem(ArmorMaterials.ILLUMINANT_STEEL, EquipmentSlot.FEET, new Item.Properties().tab(MineBound.MINEBOUND_TAB)));



    public static final RegistryObject<Item> TEST_SPELL = ITEMS.register("test_spell", () -> new TestSpell(new Item.Properties().tab(MineBound.MINEBOUND_TAB).stacksTo(1), MagicConfigRegistry.TEST_SPELL));

    /* Telekinetic */
    public static final RegistryObject<Item> TELEKINETIC_UTILITY_2 = ITEMS.register("magic_telekinetic_u2", () -> new TelekineticUtilitySpell(new Item.Properties().tab(MineBound.MINEBOUND_TAB).stacksTo(1), MagicConfigRegistry.TELEKINETIC_UTILITY_2));

    public static final RegistryObject<Item> MYRIAL_MACHETE = ITEMS.register("myrial_machete", () -> new SwordItem(Tiers.IRON, 5, -2.4F, new Item.Properties().durability(-1).tab(MineBound.MINEBOUND_TAB)));

    /* Shield */
    public static final RegistryObject<Item> SHIELD_UTILITY_2 = ITEMS.register("magic_shield_u2", () -> new ShieldUtilitySpell(new Item.Properties().tab(MineBound.MINEBOUND_TAB).stacksTo(1), MagicConfigRegistry.SHIELD_UTILITY_2));
    public static final RegistryObject<Item> SHIELD_UTILITY_3 = ITEMS.register("magic_shield_u3", () -> new ShieldUtilitySpell(new Item.Properties().tab(MineBound.MINEBOUND_TAB).stacksTo(1), MagicConfigRegistry.SHIELD_UTILITY_3));
    public static final RegistryObject<Item> SHIELD_UTILITY_4 = ITEMS.register("magic_shield_u4", () -> new ShieldUtilitySpell(new Item.Properties().tab(MineBound.MINEBOUND_TAB).stacksTo(1), MagicConfigRegistry.SHIELD_UTILITY_4));

}
