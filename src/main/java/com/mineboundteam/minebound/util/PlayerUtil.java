package com.mineboundteam.minebound.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PlayerUtil {
    public static Vec3 getLookDirection(Entity player) {
        double xRad = player.getXRot() * Math.PI / 180d;
        double yRad = player.getYRot() * Math.PI / 180d;
        double x = 0 - Math.sin(yRad);
        double y = 0 - Math.sin(xRad);
        double z = Math.cos(yRad);
        return new Vec3(x, y, z);
    }

    public static Vec3 getHandPos(Entity player, InteractionHand usedHand) {
        Vec3 lookDirection = getLookDirection(player);
        int hand = usedHand == InteractionHand.MAIN_HAND ? 1 : -1;
        return new Vec3(
                player.getX() - (lookDirection.z / 2.5d * hand),
                player.getEyeY() - 1d,
                player.getZ() + (lookDirection.x / 2.5d * hand)
        );
    }

    public static Vec3 getShift(Entity player, InteractionHand hand, Entity entity, double distMult) {
        return player.getLookAngle().scale(distMult).add(PlayerUtil.getHandPos(player, hand)).subtract(entity.position());
    }
}