package com.mineboundteam.minebound.worldgen.registry;

import java.lang.reflect.Field;
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

    protected static Field STEP = null;

    static{
        try {
            STEP = StructureFeature.class.getDeclaredField("STEP");
            STEP.setAccessible(true);
        } catch (Exception e){
            Minecraft.crash(new CrashReport("Could not extract decoration step hashmap field.", e));
        }
        
        
    }

    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MineBound.MOD_ID); 
    
    public static final StructureFeature<JigsawConfiguration> MYRIAL_BASE = new MyrialBase(JigsawConfiguration.CODEC, (e)-> true);
    
    @SuppressWarnings("unchecked")
    public static void register(IEventBus iEventBus){
        Map<StructureFeature<?>, GenerationStep.Decoration> stepMap = null;
       
        try {
            stepMap = (Map<StructureFeature<?>, GenerationStep.Decoration>)STEP.get(StructureFeature.VILLAGE);
        } catch (IllegalArgumentException | IllegalAccessException | ClassCastException e) {
            Minecraft.crash(new CrashReport("Could not extract decoration step hashmap from StructureFeature", e));
        }
        
        STRUCTURES.register("myrial_base", () -> MYRIAL_BASE);
        stepMap.put(MYRIAL_BASE, Decoration.UNDERGROUND_STRUCTURES);
        
        STRUCTURES.register(iEventBus);
        
        
    }
}
