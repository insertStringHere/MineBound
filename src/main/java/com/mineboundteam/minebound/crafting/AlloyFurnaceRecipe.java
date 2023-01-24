package com.mineboundteam.minebound.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import com.mineboundteam.minebound.registry.RecipeRegistry;

public class AlloyFurnaceRecipe extends AbstractCookingRecipe {
    public AlloyFurnaceRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(RecipeRegistry.ALLOY_FURNACE_RECIPE.get(), pId, pGroup, pIngredient, pResult, pExperience, pCookingTime);
    }  

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ALLOY_FURNACE_SERIALIZER.get();
    }

    // The crafting classes were written weird; they can't easily be extended with how they were made
    public RecipeType<?> getType(){ return type; }
    public ResourceLocation getId() { return id; }
    public String getGroup() { return group; }
    public Ingredient getIngredient() { return ingredient; }
    public ItemStack getResult() { return result; }
    public float getExperience() { return experience; }
    public int getCookingTime() { return cookingTime; }
}
