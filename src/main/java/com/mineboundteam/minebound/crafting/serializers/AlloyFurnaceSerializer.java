package com.mineboundteam.minebound.crafting.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mineboundteam.minebound.crafting.AlloyFurnaceRecipe;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class AlloyFurnaceSerializer<T> extends ForgeRegistryEntry<RecipeSerializer<?>>
        implements RecipeSerializer<AlloyFurnaceRecipe> {

    private final int defaultCookingTime;

    public AlloyFurnaceSerializer(int cookingtime) {
        defaultCookingTime = cookingtime;
    }

    @Override
    public AlloyFurnaceRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        String group = GsonHelper.getAsString(jsonObject, "group", "");
        JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
        ItemStack itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));

        NonNullList<Ingredient> ingredients = NonNullList.withSize(3, Ingredient.EMPTY);
        for(int i = 0; i < jsonArray.size() && i < 3; i++){
            ingredients.set(i, Ingredient.fromJson(jsonArray.get(i)));
        }

        float f = GsonHelper.getAsFloat(jsonObject, "experience", 0.0F);
        int i = GsonHelper.getAsInt(jsonObject, "cookingtime", this.defaultCookingTime);

        return new AlloyFurnaceRecipe(resourceLocation, group, ingredients, itemStack, f, i);
    }

    @Override
    public AlloyFurnaceRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
        String group = friendlyByteBuf.readUtf();
        int i = friendlyByteBuf.readInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
        
        for(int j = 0; j < i; j++){
            ingredients.set(j, Ingredient.fromNetwork(friendlyByteBuf));
        }
        ItemStack itemStack = friendlyByteBuf.readItem();
        float exp = friendlyByteBuf.readFloat();
        int cookingTime = friendlyByteBuf.readInt();
        return new AlloyFurnaceRecipe(resourceLocation, group, ingredients, itemStack, exp, cookingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, AlloyFurnaceRecipe pRecipe) {
        pBuffer.writeUtf(pRecipe.getGroup());
        pBuffer.writeInt(pRecipe.getIngredients().size());
        for(Ingredient ingredient : pRecipe.getIngredients()) { ingredient.toNetwork(pBuffer); }
        pBuffer.writeItemStack(pRecipe.getResult(), false);
        pBuffer.writeFloat(pRecipe.getExperience());
        pBuffer.writeInt(pRecipe.getCookingTime());
    }
}
