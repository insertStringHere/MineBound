package com.mineboundteam.minebound.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import com.mineboundteam.minebound.registry.RecipeRegistry;

public class AlloyFurnaceRecipe extends AbstractCookingRecipe {
    protected NonNullList<Ingredient> ingredients; 

    public AlloyFurnaceRecipe(ResourceLocation pId, String pGroup, NonNullList<Ingredient> pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(RecipeRegistry.ALLOY_FURNACE_RECIPE.get(), pId, pGroup, null, pResult, pExperience, pCookingTime);
        ingredients = pIngredient; 
    }  

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ALLOY_FURNACE_SERIALIZER.get();
    }

    protected static final int[] slotIds = new int[]{0, 3, 4};
    @Override
    public boolean matches(Container pInv, Level pLevel){
        
        NonNullList<ItemStack> items = NonNullList.create();
        for(int i : slotIds)
            if(!pInv.getItem(i).isEmpty())
                items.add(pInv.getItem(i));
        
        return net.minecraftforge.common.util.RecipeMatcher.findMatches(items, ingredients) != null;
    }

    // The crafting classes were written weird; they can't easily be extended with how they were made
    public RecipeType<?> getType(){ return type; }
    public ResourceLocation getId() { return id; }
    public String getGroup() { return group; }
    public NonNullList<Ingredient> getIngredients() { return ingredients; }
    public ItemStack getResult() { return result; }
    public float getExperience() { return experience; }
    public int getCookingTime() { return cookingTime; }
}
