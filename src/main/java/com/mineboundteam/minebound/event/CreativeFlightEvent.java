package com.mineboundteam.minebound.event;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class CreativeFlightEvent {
    @SubscribeEvent
    public static void toggleFlightCapability(PlayerInteractEvent.RightClickItem event) {
        if (!event.getEntity().level.isClientSide()) {
            Player player = event.getPlayer();
            if (player.getMainHandItem().getItem() == Items.FEATHER) {
                // Toggle the ability for the player to use creative flight
                player.getAbilities().mayfly = !player.getAbilities().mayfly;
                // Execute updates so ability change takes effect
                player.onUpdateAbilities();
                player.sendMessage(new TextComponent("Flying " + (player.getAbilities().mayfly ? "enabled" : "disabled")), UUID.randomUUID());
                // No need to do anything special to get around allow-flight=false on servers, tested it myself
            }
        }
    }
}
