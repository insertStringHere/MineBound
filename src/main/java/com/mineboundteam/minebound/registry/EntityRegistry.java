package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.entities.MyriCorpse;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MineBound.MOD_ID);

    public static final RegistryObject<EntityType<MyriCorpse>> MYRI_CORPSE = ENTITIES.register("myri_corpse", () -> EntityType.Builder.<MyriCorpse>of(MyriCorpse::new, MobCategory.MISC).build("myri_corpse"));

    public static void registerAttributes(EntityAttributeCreationEvent event){     
        event.put(MYRI_CORPSE.get(), DefaultAttributes.getSupplier(EntityType.ARMOR_STAND));
    }
}
