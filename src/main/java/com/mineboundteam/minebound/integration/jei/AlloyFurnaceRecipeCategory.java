package com.mineboundteam.minebound.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.block.registry.BlockRegistry;
import com.mineboundteam.minebound.crafting.AlloyFurnaceRecipe;
import com.mineboundteam.minebound.inventory.registry.RecipeRegistry;
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.common.plugins.vanilla.cooking.AbstractCookingCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class AlloyFurnaceRecipeCategory extends AbstractCookingCategory<AlloyFurnaceRecipe> {
    public static final ResourceLocation texture = new ResourceLocation(MineBound.MOD_ID, "textures/jei/alloy_furnace.png");

    private final IDrawable background;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;

    public AlloyFurnaceRecipeCategory(IGuiHelper guiHelper){
        super(guiHelper, BlockRegistry.ALLOY_FURNACE.get(), "container.alloy_furnace", 200);
        background = guiHelper.createDrawable(texture, 0, 0, 176, 91);

        this.staticFlame = guiHelper.createDrawable(texture, 176, 8, 19, 16);
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true); 

        this.cachedArrows = CacheBuilder.newBuilder()
			.maximumSize(25)
			.build(new CacheLoader<>() {
				@Override
				public IDrawableAnimated load(Integer cookTime) {
					return guiHelper.drawableBuilder(texture, 176, 0, 26, 8)
						.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
				}
			});
    }

    @Override
    protected IDrawableAnimated getArrow(AlloyFurnaceRecipe recipe) {
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = 200;
		}
		return this.cachedArrows.getUnchecked(cookTime);
	}

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public ResourceLocation getUid() {
        return RecipeRegistry.ALLOY_FURNACE_RECIPE.getId();
    }

    @Override
	public void draw(AlloyFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		animatedFlame.draw(poseStack, 43, 41);

		IDrawableAnimated arrow = getArrow(recipe);
		arrow.draw(poseStack, 99, 38);

		drawExperience(recipe, poseStack, 5);
		drawCookTime(recipe, poseStack, 62);
	}

    @Override
    protected void drawCookTime(AlloyFurnaceRecipe recipe, PoseStack poseStack, int y) {
		int cookTime = recipe.getCookingTime();
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			TranslatableComponent timeString = new TranslatableComponent("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			fontRenderer.draw(poseStack, timeString, 134, y, 0xFF808080);
		}
	}

    @Override
    protected void drawExperience(AlloyFurnaceRecipe recipe, PoseStack poseStack, int y) {
		float experience = recipe.getExperience();
		if (experience > 0) {
			TranslatableComponent experienceString = new TranslatableComponent("gui.jei.category.smelting.experience", experience);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(experienceString);
			fontRenderer.draw(poseStack, experienceString, 134, y, 0xFF808080);
		}
	}

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlloyFurnaceRecipe recipe, IFocusGroup focusGroup){
        builder.addSlot(RecipeIngredientRole.INPUT, 22, 34).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 21).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 68, 34).addIngredients(recipe.getIngredients().get(2));        

        builder.addSlot(RecipeIngredientRole.OUTPUT, 139, 34).addItemStack(recipe.getResultItem());

    }

    @Override
    public Class<? extends AlloyFurnaceRecipe> getRecipeClass() {
        return AlloyFurnaceRecipe.class;
    }
    
}
