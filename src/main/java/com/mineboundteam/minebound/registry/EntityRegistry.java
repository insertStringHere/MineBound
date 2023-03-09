package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.entity.MyriCorpse;
import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MineBound.MOD_ID);

    public static final RegistryObject<EntityType<MyrialSwordEntity>> MYRIAL_SWORD_ENTITY = ENTITIES.register("myrial_sword_entity", () -> EntityType.Builder.of(MyrialSwordEntity::new, MobCategory.MISC).build("myrial_sword_entity"));
    public static final RegistryObject<EntityType<MyriCorpse>> MYRI_CORPSE = ENTITIES.register("myri_corpse", () -> EntityType.Builder.<MyriCorpse>of(MyriCorpse::new, MobCategory.MISC).sized(2f, .6f).build("myri_corpse"));

    public static void registerAttributes(EntityAttributeCreationEvent event){     
        event.put(MYRI_CORPSE.get(), DefaultAttributes.getSupplier(EntityType.ARMOR_STAND));
    }
}
