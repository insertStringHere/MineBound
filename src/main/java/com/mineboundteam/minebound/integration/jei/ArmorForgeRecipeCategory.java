package com.mineboundteam.minebound.integration.jei;

import com.mineboundteam.minebound.block.registry.BlockRegistry;
import com.mineboundteam.minebound.inventory.registry.RecipeRegistry;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;

public class ArmorForgeRecipeCategory implements IRecipeCategory<Recipe<Container>>{

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
        return JEIIntegration.helpers.getGuiHelper().createDrawableItemStack(ForgeRegistries.ITEMS.getValue(BlockRegistry.ARMOR_FORGE.getId()).getDefaultInstance());
    }

    @Override
    public ResourceLocation getUid() {
        return RecipeRegistry.ARMOR_FORGE_RECIPE.getId();
    }

    @Override
    public Class<? extends Recipe<Container>> getRecipeClass() {
        return com.mineboundteam.minebound.crafting.ArmorForgeRecipe.class;
    }
    
}