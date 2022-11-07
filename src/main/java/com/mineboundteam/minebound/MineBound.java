package com.mineboundteam.minebound;


import com.mineboundteam.minebound.registry.BlockRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mineboundteam.minebound.registry.Registry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(MineBound.MOD_ID)
public class MineBound {
    public static final String MOD_ID = "minebound";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final CreativeModeTab MINEBOUND_TAB = new CreativeModeTab(MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        @Override
        public @NotNull ItemStack makeIcon() { return new ItemStack(BlockRegistry.BOOM_BUTTON.get()); }
    };

    public MineBound(){
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        Registry.RegisterMod(iEventBus);

        iEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent fmlClientSetupEvent) {
       
    }
}