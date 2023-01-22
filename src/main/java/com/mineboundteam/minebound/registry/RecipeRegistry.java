package com.mineboundteam.minebound.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.mineboundteam.minebound.MineBound.MOD_ID;

import com.mineboundteam.minebound.crafting.AlloyFurnaceRecipe;
import com.mineboundteam.minebound.crafting.serializers.AlloyFurnaceSerializer;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static final RegistryObject<RecipeSerializer<AlloyFurnaceRecipe>> ALLOY_FURNACE_SERIALIZER = RECIPE_SERIALIZERS.register("alloy_furnace", () -> new AlloyFurnaceSerializer<>(200));

    public static final RecipeType<AlloyFurnaceRecipe> ALLOY_FURNACE_RECIPE = RecipeType.register("alloy_furnace");

}
