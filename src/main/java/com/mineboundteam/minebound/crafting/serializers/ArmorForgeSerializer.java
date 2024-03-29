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
        JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ItemStack armor = ItemStack.EMPTY; 
        try{
            armor = ShapedRecipe.itemStackFromJson((GsonHelper.getAsJsonObject(jsonObject, "armor")));
        } catch(Exception e){/* don't care */}

        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));

        for(int i = 0; i < jsonArray.size() && i < 5; i++){
            ingredients.add(Ingredient.fromJson(jsonArray.get(i)));
        }

        return new ArmorForgeRecipe(resourceLocation, ingredients, armor, output);
    }

    @Override
    public ArmorForgeRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
        int size = friendlyByteBuf.readInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);

        for(int i = 0; i < size; i++){
            ingredients.set(i, Ingredient.fromNetwork(friendlyByteBuf));
        }
        ItemStack armor = friendlyByteBuf.readItem();
        ItemStack output = friendlyByteBuf.readItem();
        return new ArmorForgeRecipe(resourceLocation, ingredients, armor, output);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, ArmorForgeRecipe armorForgeRecipe) {
        friendlyByteBuf.writeInt(armorForgeRecipe.getIngredients().size());
        for (Ingredient ingredient : armorForgeRecipe.getIngredients()) {
            ingredient.toNetwork(friendlyByteBuf);
        }
        friendlyByteBuf.writeItemStack(armorForgeRecipe.getArmor(), false);
        friendlyByteBuf.writeItemStack(armorForgeRecipe.getResultItem(), false);
    }
}
