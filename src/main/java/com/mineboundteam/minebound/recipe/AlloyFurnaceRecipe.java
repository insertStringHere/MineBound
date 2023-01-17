package com.mineboundteam.minebound.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.mineboundteam.minebound.MineBound.MOD_ID;

public class AlloyFurnaceRecipe implements Recipe<SimpleContainer> {
    private final ItemStack itemStack;
    private final NonNullList<Ingredient> ingredients;
    private final ResourceLocation resourceLocation;

    public AlloyFurnaceRecipe(ResourceLocation resourceLocation, ItemStack itemStack, NonNullList<Ingredient> ingredients) {
        this.itemStack = itemStack;
        this.ingredients = ingredients;
        this.resourceLocation = resourceLocation;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer simpleContainer) {
        return itemStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
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
        return itemStack.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.TYPE;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        return ingredients.get(0).test(simpleContainer.getItem(0));
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<AlloyFurnaceRecipe> {
        public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(MOD_ID, "alloy_furnace");
        public static final Serializer SERIALIZER = new Serializer();

        private static <G> Class<G> castClass(Class<?> temp) {
            return (Class<G>) temp;
        }

        @Override
        public @NotNull AlloyFurnaceRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            ItemStack itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));
            NonNullList<Ingredient> ingredients = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromJson(jsonArray.get(i)));
            }

            return new AlloyFurnaceRecipe(resourceLocation, itemStack, ingredients);
        }

        @Override
        public @Nullable AlloyFurnaceRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(friendlyByteBuf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromNetwork(friendlyByteBuf));
            }
            ItemStack itemStack = friendlyByteBuf.readItem();

            return new AlloyFurnaceRecipe(resourceLocation, itemStack, ingredients);
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return RESOURCE_LOCATION;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeType.class);
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return SERIALIZER;
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, AlloyFurnaceRecipe alloyFurnaceRecipe) {
            friendlyByteBuf.writeInt(alloyFurnaceRecipe.getIngredients().size());
            for (Ingredient ing : alloyFurnaceRecipe.getIngredients()) {
                ing.toNetwork(friendlyByteBuf);
            }
            friendlyByteBuf.writeItemStack(alloyFurnaceRecipe.getResultItem(), false);
        }
    }

    public static class Type implements RecipeType<AlloyFurnaceRecipe> {
        public static final String ID = "alloy_furnace";
        public static final Type TYPE = new Type();
        private Type() {}
    }
}
