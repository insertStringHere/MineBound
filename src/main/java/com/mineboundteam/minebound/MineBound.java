package com.mineboundteam.minebound;


import com.mineboundteam.minebound.registry.BlockRegistry;
import com.mineboundteam.minebound.registry.EntityRegistry;
import com.mineboundteam.minebound.registry.RendererRegistry;
import com.mineboundteam.minebound.registry.ItemRegistry;
import com.mineboundteam.minebound.registry.KeyRegistry;
import com.mineboundteam.minebound.registry.Registry;

import com.mineboundteam.minebound.container.AlloyFurnaceScreen;
import com.mineboundteam.minebound.registry.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(MineBound.MOD_ID)
public class MineBound {
    public static final String MOD_ID = "minebound";
    public static final CreativeModeTab MINEBOUND_TAB = new CreativeModeTab(MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        @Override
        public @NotNull ItemStack makeIcon() { return new ItemStack(ItemRegistry.MYRI_MANASAC.get()); }
    };

    public MineBound(){
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Registry.RegisterMod(iEventBus);

        iEventBus.addListener(this::clientSetup);
        iEventBus.addListener(EntityRegistry::registerAttributes);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent fmlClientSetupEvent) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.MYRIAL_GLASS.get(), RenderType.translucent());
        RendererRegistry.register();
        KeyRegistry.register();
        MenuScreens.register(ContainerRegistry.ALLOY_FURNACE_CONTAINER.get(), AlloyFurnaceScreen::new);
    }
}
