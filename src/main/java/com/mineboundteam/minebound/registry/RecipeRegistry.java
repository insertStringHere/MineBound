package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.recipe.AlloyFurnaceRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.mineboundteam.minebound.MineBound.MOD_ID;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static final RegistryObject<RecipeSerializer<AlloyFurnaceRecipe>> ALLOY_FURNACE_SERIALIZER = RECIPE_SERIALIZERS.register("alloy_furnace", () -> AlloyFurnaceRecipe.Serializer.SERIALIZER);
}
