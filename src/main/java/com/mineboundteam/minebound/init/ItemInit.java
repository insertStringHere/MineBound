package com.mineboundteam.minebound.init;

import com.google.common.base.Supplier;
import com.mineboundteam.minebound.MineBound;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MineBound.MOD_ID);
    public static final RegistryObject<Item> JELLO = register("jello", () -> new SwordItem(Tiers.DIAMOND, 11, 2f, new Item.Properties().tab(MineBound.MINEBOUND_TAB).food(new FoodProperties.Builder().nutrition(8).saturationMod(14.4f).alwaysEat().effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 0.5F).build())));

    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
}
