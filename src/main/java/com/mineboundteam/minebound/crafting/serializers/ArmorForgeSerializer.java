package com.mineboundteam.minebound.crafting.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mineboundteam.minebound.crafting.ArmorForgeRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class ArmorForgeSerializer<T> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ArmorForgeRecipe> {
    @Override
    public @NotNull ArmorForgeRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
        String group = GsonHelper.getAsString(jsonObject, "group", "");
        JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));

        for(int i = 0; i < jsonArray.size() && i < 3; i++){
            ingredients.add(Ingredient.fromJson(jsonArray.get(i)));
        }

        return new ArmorForgeRecipe(resourceLocation, group, ingredients, output);
    }

    @Override
    public ArmorForgeRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
        String group = friendlyByteBuf.readUtf();
        int size = friendlyByteBuf.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
        ItemStack output = friendlyByteBuf.readItem();

        for(int i = 0; i < size; i++){
            ingredients.add(Ingredient.fromNetwork(friendlyByteBuf));
        }

        return new ArmorForgeRecipe(resourceLocation, group, ingredients, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf friendlyByteBuf, ArmorForgeRecipe armorForgeRecipe) {
        friendlyByteBuf.writeUtf(armorForgeRecipe.getGroup());
        for (Ingredient ingredient : armorForgeRecipe.getIngredients()) {
            ingredient.toNetwork(friendlyByteBuf);
        }
        friendlyByteBuf.writeItem(armorForgeRecipe.getResultItem());
    }
}
