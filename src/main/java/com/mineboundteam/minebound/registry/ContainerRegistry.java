package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.container.AlloyFurnaceContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MineBound.MOD_ID);
    public static final RegistryObject<MenuType<AlloyFurnaceContainer>> ALLOY_FURNACE_CONTAINER = CONTAINERS.register("alloy_furnace_container", () -> IForgeMenuType.create(AlloyFurnaceContainer::new));
}
