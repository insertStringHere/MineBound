package com.mineboundteam.minebound.integration.jei;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.block.registry.BlockRegistry;
import com.mineboundteam.minebound.crafting.ArmorForgeRecipe;
import com.mineboundteam.minebound.inventory.registry.RecipeRegistry;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class ArmorForgeRecipeCategory implements IRecipeCategory<ArmorForgeRecipe>{
    public static final ResourceLocation texture = new ResourceLocation(MineBound.MOD_ID, "textures/jei/armor_forge.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ArmorForgeRecipeCategory(IGuiHelper guiHelper){
        background = guiHelper.createDrawable(texture, 0, 0, 176, 128);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.ARMOR_FORGE.get()));
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("container.armor_forge");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon; 
    }

    @Override
    public ResourceLocation getUid() {
        return RecipeRegistry.ARMOR_FORGE_RECIPE.getId();
    }

    @Override
    public Class<? extends ArmorForgeRecipe> getRecipeClass() {
        return ArmorForgeRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ArmorForgeRecipe recipe, IFocusGroup focusGroup){
        builder.addSlot(RecipeIngredientRole.INPUT, 51, 19).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 42).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 83, 42).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 33, 81).addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 69, 81).addIngredients(recipe.getIngredients().get(4));
        
        builder.addSlot(RecipeIngredientRole.INPUT, 51, 49).addItemStack(recipe.getArmor());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 144, 52).addItemStack(recipe.getResultItem());

    }
    
}