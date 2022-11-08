package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.structures.NetherTemple;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class StructureRegistry {
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MineBound.MOD_ID);

    public static final RegistryObject<StructureFeature<?>> NETHER_TEMPLE = STRUCTURES.register("nether_temple", NetherTemple::new);
    
}
