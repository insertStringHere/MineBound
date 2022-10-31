package com.mineboundteam.minebound.event;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class MoveEntityEvent {
    @SubscribeEvent
    public static void pushEntity(PlayerInteractEvent.EntityInteract event) {
        if (!event.getEntity().level.isClientSide()) {
            Player player = event.getPlayer();
            // Push entity if right-clicked on with a stick
            if (player.getMainHandItem().getItem() == Items.STICK) {
                // IMPORTANT CODE
                Entity targetEntity = event.getTarget();
                // How far to move the entity
                // distance in blocks ~= intensity * 2.5
                float intensity = 10f;
                // The direction the player is looking
                float direction = player.getRotationVector().y;
                // Calculate the delta X and Z to move the entity on the XZ plane based on where the player is looking and intensity
                float motionX = (float) (0 - Math.sin(direction * Math.PI / 180f)) * intensity;
                float motionZ = (float) (Math.cos(direction * Math.PI / 180f)) * intensity;
                // Push the entity using the calculated values
                // Entity#setDeltaMovement(float x, float y, float z);
                targetEntity.setDeltaMovement(motionX,0,motionZ);
            }
        }
    }

    @SubscribeEvent
    public static void liftAndDropEntity(PlayerInteractEvent.EntityInteract event) {
        if (!event.getEntity().level.isClientSide()) {
            Player player = event.getPlayer();
            // Push entity if right-clicked on with a stick
            if (player.getMainHandItem().getItem() == Items.BLAZE_ROD) {
                Entity targetEntity = event.getTarget();
                // Push entity up into the air and let them fall
                // Entity#setDeltaMovement(float x, float y, float z);
                targetEntity.setDeltaMovement(0,2f,0);
            }
        }
    }
}
