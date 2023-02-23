package com.mineboundteam.minebound.crafting;

import com.mineboundteam.minebound.registry.RecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ArmorForgeRecipe implements Recipe<Container> {
    private final ItemStack armor;
    private final NonNullList<Ingredient> ingredients;
    private final ResourceLocation resourceLocation;
    private final ItemStack output;

    public ArmorForgeRecipe(ResourceLocation resourceLocation, NonNullList<Ingredient> ingredients, ItemStack armor, ItemStack output) {
        this.armor = armor;
        this.resourceLocation = resourceLocation;
        this.output = output;
        this.ingredients = ingredients;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public ItemStack getArmor() {
        return armor.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return resourceLocation;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public boolean matches(@NotNull Container container, Level level) {
        if (level.isClientSide) return false;
        NonNullList<ItemStack> itemStacks = NonNullList.create();

        for (int i = 0; i < 5; i++)
            if (!container.getItem(i).isEmpty())
                itemStacks.add(container.getItem(i));

        return net.minecraftforge.common.util.RecipeMatcher.findMatches(itemStacks, ingredients) != null 
            && container.getItem(5).sameItem(this.armor);
    }
    public boolean matches(Container container, Container ArmorContainer, Level level) {
        if (level.isClientSide) return false;
        NonNullList<ItemStack> itemStacks = NonNullList.create();

        for (int i = 0; i < 5; i++)
            if (!container.getItem(i).isEmpty())
                itemStacks.add(container.getItem(i));

        return net.minecraftforge.common.util.RecipeMatcher.findMatches(itemStacks, ingredients) != null 
            && ArmorContainer.getItem(0).sameItem(this.armor);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ARMOR_FORGE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<ArmorForgeRecipe> getType() {
        return RecipeRegistry.ARMOR_FORGE_RECIPE.get();
    }
}
