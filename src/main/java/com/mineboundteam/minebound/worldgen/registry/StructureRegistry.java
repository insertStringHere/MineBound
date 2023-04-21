package com.mineboundteam.minebound.worldgen.registry;

import java.util.Map;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.worldgen.MyrialBase;

import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureRegistry {

    protected static Map<StructureFeature<?>, GenerationStep.Decoration> STEP = null;

    static{
        try {
            var hashField = StructureFeature.class.getDeclaredField("STEP");
            hashField.setAccessible(true);
            STEP = (Map<StructureFeature<?>, GenerationStep.Decoration>)hashField.get(StructureFeature.VILLAGE);
        } catch (Exception e){
            Minecraft.crash(new CrashReport("Not my fault this game is coded like garbage", e));
        }
        
        
    }

    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MineBound.MOD_ID); 
    
    public static final StructureFeature<JigsawConfiguration> MYRIAL_BASE = new MyrialBase(JigsawConfiguration.CODEC, (e)-> true);

    public static void register(IEventBus iEventBus){
        STRUCTURES.register("myrial_base", () -> MYRIAL_BASE);
        STEP.put(MYRIAL_BASE, Decoration.UNDERGROUND_STRUCTURES);
        
        STRUCTURES.register(iEventBus);
        
        
    }
}
