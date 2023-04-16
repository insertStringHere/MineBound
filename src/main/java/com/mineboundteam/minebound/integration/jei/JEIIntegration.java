package com.mineboundteam.minebound.integration.jei;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.block.registry.BlockRegistry;
import com.mineboundteam.minebound.crafting.AlloyFurnaceRecipe;
import com.mineboundteam.minebound.crafting.ArmorForgeRecipe;
import com.mineboundteam.minebound.inventory.AlloyFurnaceMenu;
import com.mineboundteam.minebound.inventory.ArmorForgeMenu;
import com.mineboundteam.minebound.inventory.registry.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class JEIIntegration implements IModPlugin{

    public static final RecipeType<ArmorForgeRecipe> ARMOR_FORGE = new RecipeType<>(RecipeRegistry.ARMOR_FORGE_RECIPE.getId(), ArmorForgeRecipe.class);
    public static final RecipeType<AlloyFurnaceRecipe> ALLOY_FURNACE = new RecipeType<>(RecipeRegistry.ALLOY_FURNACE_RECIPE.getId(), AlloyFurnaceRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MineBound.MOD_ID, "jei");
    }

    @Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ArmorForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AlloyFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

    @Override
    @SuppressWarnings("resource")
	public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) return;

        RecipeManager rm = world.getRecipeManager();
        
        registration.addRecipes(ARMOR_FORGE, rm.getAllRecipesFor(RecipeRegistry.ARMOR_FORGE_RECIPE.get()));
        registration.addRecipes(ALLOY_FURNACE, rm.getAllRecipesFor(RecipeRegistry.ALLOY_FURNACE_RECIPE.get()));          
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ARMOR_FORGE.get()), ARMOR_FORGE);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ALLOY_FURNACE.get()), ALLOY_FURNACE);
	}

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ArmorForgeMenu.class, ARMOR_FORGE, 40, 6, 0, 40);
        registration.addRecipeTransferHandler(AlloyFurnaceMenu.class, ALLOY_FURNACE, 0, 3, 5, 36);
	}

    
}
