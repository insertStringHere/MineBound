package com.mineboundteam.minebound.event;


import com.mineboundteam.minebound.MineBound;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class Events {

    @SubscribeEvent
    public static void destroyBlock(PlayerInteractEvent.LeftClickBlock event) {
        if(!event.getEntity().level.isClientSide()) {
            Player player = event.getPlayer();
            // Destroy block if clicked on with a stick
            if(player.getMainHandItem().getItem() == Items.STICK) {
                // This is really the important code we will likely need down the line
                // World#destroyBlock(BlockPos, boolean dropBlock)
                event.getWorld().destroyBlock(event.getPos(), true);
            }
        }
    }
}
