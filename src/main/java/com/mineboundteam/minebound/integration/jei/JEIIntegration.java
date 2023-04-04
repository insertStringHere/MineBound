package com.mineboundteam.minebound.integration.jei;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.crafting.AlloyFurnaceRecipe;
import com.mineboundteam.minebound.crafting.ArmorForgeRecipe;
import com.mineboundteam.minebound.inventory.registry.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIIntegration implements IModPlugin{

    public static IJeiHelpers helpers;

    public static RecipeType<AlloyFurnaceRecipe> ALLOY_FURNACE_RECIPE_TYPE = RecipeType.create(MineBound.MOD_ID, "alloy_furnace", AlloyFurnaceRecipe.class); 
    public static RecipeType<ArmorForgeRecipe> ARMOR_FORGE_RECIPE_TYPE = RecipeType.create(MineBound.MOD_ID, "armor_forge", ArmorForgeRecipe.class); 

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MineBound.MOD_ID, "jei");
    }

    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        helpers = jeiRuntime.getJeiHelpers();
	}

    @Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ArmorForgeRecipeCategory());
        registration.addRecipeCategories(new AlloyFurnaceRecipeCategory());
	}

    @Override
    @SuppressWarnings("resource")
	public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) return;
        
        registration.addRecipes(ARMOR_FORGE_RECIPE_TYPE, world.getRecipeManager().getAllRecipesFor(RecipeRegistry.ARMOR_FORGE_RECIPE.get()));
        registration.addRecipes(ALLOY_FURNACE_RECIPE_TYPE, world.getRecipeManager().getAllRecipesFor(RecipeRegistry.ALLOY_FURNACE_RECIPE.get()));
    }

    
}
