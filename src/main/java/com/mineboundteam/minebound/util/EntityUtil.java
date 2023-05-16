package com.mineboundteam.minebound.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.function.Predicate;

public class EntityUtil {
    /**
     * Derived from {@link net.minecraft.world.entity.projectile.ProjectileUtil#getEntityHitResult(Level, Entity, Vec3, Vec3, AABB, Predicate, float)}
     */
    public static ArrayList<EntityHitResult> multiEntityHitResult(Level level, Projectile projectile, AABB projectileBB,
                                                                  Predicate<Entity> filter, double inflation) {
        ArrayList<EntityHitResult> entityHitResults = new ArrayList<>();

        for (Entity entity : level.getEntities(projectile, projectileBB.inflate(inflation), filter)) {
            entityHitResults.add(new EntityHitResult(entity));
        }

        return entityHitResults;
    }
}
