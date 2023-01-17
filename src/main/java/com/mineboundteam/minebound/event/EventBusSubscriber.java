package com.mineboundteam.minebound.event;

import com.mineboundteam.minebound.recipe.AlloyFurnaceRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.mineboundteam.minebound.MineBound.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventBusSubscriber {
    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, AlloyFurnaceRecipe.Type.ID, AlloyFurnaceRecipe.Type.TYPE);
    }
}
