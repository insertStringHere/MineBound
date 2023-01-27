package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.crafting.AlloyFurnaceRecipe;
import com.mineboundteam.minebound.crafting.ArmorForgeRecipe;
import com.mineboundteam.minebound.crafting.serializers.AlloyFurnaceSerializer;
import com.mineboundteam.minebound.crafting.serializers.ArmorForgeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.mineboundteam.minebound.MineBound.MOD_ID;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static final RegistryObject<RecipeSerializer<AlloyFurnaceRecipe>> ALLOY_FURNACE_SERIALIZER = RECIPE_SERIALIZERS.register("alloy_furnace", () -> new AlloyFurnaceSerializer<>(200));
    public static final RegistryObject<RecipeSerializer<ArmorForgeRecipe>> ARMOR_FORGE_SERIALIZER = RECIPE_SERIALIZERS.register("armor_forge", ArmorForgeSerializer::new);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, MOD_ID);
    public static final RegistryObject<RecipeType<AlloyFurnaceRecipe>> ALLOY_FURNACE_RECIPE =  RECIPE_TYPE.<RecipeType<AlloyFurnaceRecipe>>register("alloy_furnace", () -> new RecipeType<AlloyFurnaceRecipe>(){});
    public static final RegistryObject<RecipeType<ArmorForgeRecipe>> ARMOR_FORGE_RECIPE =  RECIPE_TYPE.register("armor_forge", () -> new RecipeType<>() {

    });
}
