package com.mineboundteam.minebound;

import com.mineboundteam.minebound.Blocks.BlockInit;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("minebound")
public class MineBound {
    public MineBound(){
        final IEventBus MEBus = FMLJavaModLoadingContext.get().getModEventBus();

        MEBus.addListener(this::setup);
        BlockInit.BLOCKS.register(MEBus);
        
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(final FMLCommonSetupEvent event){

    }

}
