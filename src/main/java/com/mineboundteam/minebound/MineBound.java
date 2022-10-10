package com.mineboundteam.minebound;

import com.mineboundteam.minebound.Blocks.BlockInit;
import com.mineboundteam.minebound.effect.ModEffects;
import com.mineboundteam.minebound.sound.ModSounds;
import com.mineboundteam.minebound.item.ModItems;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MineBound.MOD_ID)
public class MineBound {

    public static final String MOD_ID = "minebound";

    public MineBound(){
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::setup);
        BlockInit.register(eventBus);
        ModItems.register(eventBus);
        ModSounds.register(eventBus);
        ModEffects.register(eventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(final FMLCommonSetupEvent event){

    }

}
