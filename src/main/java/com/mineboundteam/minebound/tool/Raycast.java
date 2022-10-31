package com.mineboundteam.minebound.tool;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

//@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = com.mineboundteam.minebound.MineBound.MOD_ID)
public class Raycast {
    // Test method; uncomment to test
    // @net.minecraftforge.eventbus.api.SubscribeEvent
    // public static void testLook(net.minecraftforge.event.TickEvent.PlayerTickEvent e) {
    //     if (e.side == net.minecraftforge.fml.LogicalSide.SERVER) {
    //         com.mineboundteam.minebound.MineBound.LOGGER.info("{}", getLookEntity(e.player, 10));
    //     }
    // }

    public static LivingEntity getLookEntity(Entity p, int distanceTo){
        return getLookEntity(p, distanceTo, LivingEntity.class); 
    }

    public static <T extends Entity> T getLookEntity(Entity p, int distanceTo, Class<T> entityType) {
        T seen = null;

        for (Entity e : p.level.getEntities(p, p.getBoundingBox().expandTowards(p.getViewVector(1f).scale(distanceTo)))) {
            if (entityType.isAssignableFrom(e.getClass()) &&
                    canSee(p, e) &&
                    (seen == null || p.closerThan(e, p.distanceTo(seen))))
                // it is checked you tunnel-visioned pisswad.
                seen = (T)e;
        }

        return seen;
    }


    private static boolean canSee(Entity looker, Entity seen) {
        Vec3 xPlaneTop = new Vec3(seen.getBoundingBox().getCenter().x, seen.getBoundingBox().maxY,
                seen.getBoundingBox().maxZ);
        Vec3 xPlaneBottom = new Vec3(seen.getBoundingBox().getCenter().x, seen.getBoundingBox().minY,
                seen.getBoundingBox().minZ);

        double xScale = (xPlaneTop.x - looker.getEyePosition(1).x) / looker.getViewVector(1).x;
        Vec3 xVect = looker.getEyePosition(1.0F).add(looker.getViewVector(1.0F).scale(xScale));

        if (xPlaneBottom.y <= xVect.y && xVect.y <= xPlaneTop.y &&
                xPlaneBottom.z <= xVect.z && xVect.z <= xPlaneTop.z)
            return true;

        Vec3 yPlaneTop = new Vec3(seen.getBoundingBox().maxX, seen.getBoundingBox().getCenter().y,
                seen.getBoundingBox().maxZ);
        Vec3 yPlaneBottom = new Vec3(seen.getBoundingBox().minX, seen.getBoundingBox().getCenter().y,
                seen.getBoundingBox().minZ);

        double yScale = (yPlaneTop.y - looker.getEyePosition(1).y) / looker.getViewVector(1).y;
        Vec3 yVect = looker.getEyePosition(1.0F).add(looker.getViewVector(1.0F).scale(yScale));

        if (yPlaneBottom.x <= yVect.x && yVect.x <= yPlaneTop.x &&
                yPlaneBottom.z <= yVect.z && yVect.z <= yPlaneTop.z)
            return true;

        Vec3 zPlaneTop = new Vec3(seen.getBoundingBox().maxX, seen.getBoundingBox().maxY,
                seen.getBoundingBox().getCenter().z);
        Vec3 zPlaneBottom = new Vec3(seen.getBoundingBox().minX, seen.getBoundingBox().minY,
                seen.getBoundingBox().getCenter().z);

        double zScale = (zPlaneTop.z - looker.getEyePosition(1).z) / looker.getViewVector(1).z;
        Vec3 zVect = looker.getEyePosition(1.0F).add(looker.getViewVector(1.0F).scale(zScale));

        if (zPlaneBottom.x <= zVect.x && zVect.x <= zPlaneTop.x &&
                zPlaneBottom.y <= zVect.y && zVect.y <= zPlaneTop.y)
            return true;

        return false;
    }
}
