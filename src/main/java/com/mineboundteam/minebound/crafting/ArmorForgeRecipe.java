package com.mineboundteam.minebound.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ArmorForgeRecipe extends AlloyFurnaceRecipe {
    public ArmorForgeRecipe(ResourceLocation resourceLocation, String group, NonNullList<Ingredient> ingredients, ItemStack output) {
        super(resourceLocation, group, ingredients, output, 0, 0);
    }
}
