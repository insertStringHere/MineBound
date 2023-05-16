package com.mineboundteam.minebound.util;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BoundingBoxUtil {
    // Wrote these all for MyrialSwordEntity, ended up not needing them after refactoring
    // Maybe they will find some use at a later date
    public static Vec3 vectorFromMin(AABB boundingBox) {
        return new Vec3(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
    }

    public static Vec3 vectorFromMax(AABB boundingBox) {
        return new Vec3(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    }

    public static boolean intersects(AABB aabb1, AABB aabb2) {
        return aabb1.intersects(aabb2) || aabb2.intersects(aabb1);
    }
}
