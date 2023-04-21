package com.mineboundteam.minebound.entity.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.entity.FireProjectile;
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
    public static final RegistryObject<EntityType<MyrialSwordEntity>> MYRIAL_SWORD_ENTITY = ENTITIES.register("myrial_sword_entity", () -> EntityType.Builder.<MyrialSwordEntity>of(MyrialSwordEntity::new, MobCategory.MISC).build("myrial_sword_entity"));
    public static final RegistryObject<EntityType<MyriCorpse>> MYRI_CORPSE = ENTITIES.register("myri_corpse", () -> EntityType.Builder.<MyriCorpse>of(MyriCorpse::new, MobCategory.MISC).sized(2f, .6f).build("myri_corpse"));
    public static final RegistryObject<EntityType<FireProjectile>> FIRE_PROJECTILE = ENTITIES.register("fire_projectile", () -> EntityType.Builder.<FireProjectile>of(FireProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("fire_projectile"));

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(MYRI_CORPSE.get(), DefaultAttributes.getSupplier(EntityType.ARMOR_STAND));
    }
}
