package com.mineboundteam.minebound.integration.jei;

import com.mineboundteam.minebound.block.registry.BlockRegistry;
import com.mineboundteam.minebound.inventory.registry.RecipeRegistry;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraftforge.registries.ForgeRegistries;

public class AlloyFurnaceRecipeCategory implements IRecipeCategory<AbstractCookingRecipe> {

    @Override
    public Component getTitle() {
        return new TranslatableComponent("");
    }

    @Override
    public IDrawable getBackground() {
        return JEIIntegration.helpers.getGuiHelper().createDrawable(getUid(), 0, 0, 0, 0);
    }

    @Override
    public IDrawable getIcon() {
        return JEIIntegration.helpers.getGuiHelper().createDrawableItemStack(ForgeRegistries.ITEMS.getValue(BlockRegistry.ALLOY_FURNACE.getId()).getDefaultInstance());
    }

    @Override
    public ResourceLocation getUid() {
        return RecipeRegistry.ALLOY_FURNACE_RECIPE.getId();
    }

    @Override
    public Class<? extends AbstractCookingRecipe> getRecipeClass() {
        return com.mineboundteam.minebound.crafting.AlloyFurnaceRecipe.class;
    }
    
}
