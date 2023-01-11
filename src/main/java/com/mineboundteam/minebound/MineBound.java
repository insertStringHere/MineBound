package com.mineboundteam.minebound;

import com.mineboundteam.minebound.container.AlloyFurnaceScreen;
import com.mineboundteam.minebound.registry.ContainerRegistry;
import com.mineboundteam.minebound.registry.Registry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MineBound.MOD_ID)
public class MineBound {
    public static final String MOD_ID = "minebound";

    public MineBound(){
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        Registry.RegisterMod(iEventBus);

        iEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent fmlClientSetupEvent) {
        MenuScreens.register(ContainerRegistry.ALLOY_FURNACE_CONTAINER.get(), AlloyFurnaceScreen::new);
    }
}
