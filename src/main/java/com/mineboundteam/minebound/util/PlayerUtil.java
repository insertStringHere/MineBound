package com.mineboundteam.minebound.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PlayerUtil {
    public static Vec3 getLookDirection(Player player) {
        float yRot = player.getYRot();
        float xRot = player.getXRot();
        double x = 0 - Math.sin(yRot * Math.PI / 180f);
        double y = 0 - Math.sin(xRot * Math.PI / 180f);
        double z = Math.cos(yRot * Math.PI / 180f);
        return new Vec3(x, y, z);
    }

    public static Vec3 getHandPos(Player player, InteractionHand usedHand) {
        Vec3 lookDirection = getLookDirection(player);
        int hand = usedHand == InteractionHand.MAIN_HAND ? 1 : -1;
        return new Vec3(
                player.getX() - (lookDirection.z / 2.5d * hand),
                player.getEyeY() - 1d,
                player.getZ() + (lookDirection.x / 2.5d * hand)
        );
    }
}
