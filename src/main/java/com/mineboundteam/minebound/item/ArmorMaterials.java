package com.mineboundteam.minebound.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import com.mineboundteam.minebound.item.registry.ItemRegistry;

/**
 * Pretty much taken from minecraft's build-in armor materials
 */
public enum ArmorMaterials implements ArmorMaterial {
    MYRIAL_EFFIGY(0, new int[]{1, 4, 5, 2}, 0, SoundEvents.ARMOR_EQUIP_CHAIN, 0f, 0f, Ingredient.of(ItemRegistry.ENERGIZED_IRON_INGOT.get())),
    MYRIAL_SUIT(0, new int[] { 2, 5, 6, 2 }, 0, SoundEvents.ARMOR_EQUIP_IRON, 0f, 0f, Ingredient.of(ItemRegistry.ILLUMINANT_STEEL_INGOT.get())),
    MYRIAL_SYNERGY(0, new int[] { 3, 6, 8, 3 }, 0, SoundEvents.ARMOR_EQUIP_DIAMOND, 0f, 0f, Ingredient.of(ItemRegistry.GILDED_DIAMOND_INGOT.get())),
    MYRIAL_SINGULARITY(0, new int[] { 3, 6, 8, 3 }, 0, SoundEvents.ARMOR_EQUIP_NETHERITE, 3, .1f, Ingredient.of(ItemRegistry.MYRIAL_DIAMOND_INGOT.get())),
    ENERGIZED_IRON(15, new int[]{2, 5, 6, 2}, 8, SoundEvents.ARMOR_EQUIP_IRON, 0.5F, 0.0F,  Ingredient.of(ItemRegistry.ENERGIZED_IRON_INGOT.get())),
    ILLUMINANT_STEEL(30, new int[]{3, 5, 7, 2}, 8, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.0F, Ingredient.of(ItemRegistry.ILLUMINANT_STEEL_INGOT.get())),
    GILDED_DIAMOND(33, new int[] { 4, 7, 8, 3 }, 9, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, .1f,  Ingredient.of(ItemRegistry.GILDED_DIAMOND_INGOT.get())),
    MYRIAL_DIAMOND(37, new int[] { 5, 7, 9, 5 }, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 3.75f, .3f, Ingredient.of(ItemRegistry.MYRIAL_DIAMOND_INGOT.get()));

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient repairIngredient;
    
    private ArmorMaterials(int pDurabilityMultiplier, int[] pSlotProtections, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Ingredient pRepairIngredient) {
        this.durabilityMultiplier = pDurabilityMultiplier;
        this.slotProtections = pSlotProtections;
        this.enchantmentValue = pEnchantmentValue;
        this.sound = pSound;
        this.toughness = pToughness;
        this.knockbackResistance = pKnockbackResistance;
        this.repairIngredient = pRepairIngredient;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot pSlot) {
        return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
    }

    public int getDefenseForSlot(EquipmentSlot pSlot) {
        return this.slotProtections[pSlot.getIndex()];
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public @NotNull SoundEvent getEquipSound() {
        return this.sound;
    }

    public @NotNull Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }

    public @NotNull String getName() {
        return this.name().toLowerCase();
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
