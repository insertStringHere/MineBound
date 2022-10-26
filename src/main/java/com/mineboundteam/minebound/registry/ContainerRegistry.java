package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.container.SpellHolderContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MineBound.MOD_ID);
    public static final RegistryObject<MenuType<SpellHolderContainer>> SPELL_HOLDER_CONTAINER = CONTAINERS.register("spell_holder_container", () -> new MenuType<>(SpellHolderContainer::getClientContainer));
}
