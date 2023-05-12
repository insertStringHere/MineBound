package com.mineboundteam.minebound.util;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BoundingBoxUtil {
    public static Vec3 vectorFromMin(AABB boundingBox) {
        return new Vec3(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
    }

    public static Vec3 vectorFromMax(AABB boundingBox) {
        return new Vec3(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    }
}
