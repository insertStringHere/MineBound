package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.container.AlloyFurnaceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MineBound.MOD_ID);
    public static final RegistryObject<MenuType<AlloyFurnaceMenu>> ALLOY_FURNACE_CONTAINER = MENUS.register("alloy_furnace_container", () -> IForgeMenuType.create(AlloyFurnaceMenu::new));
}
