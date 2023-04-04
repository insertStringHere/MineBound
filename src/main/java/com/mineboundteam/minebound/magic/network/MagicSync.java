package com.mineboundteam.minebound.magic.network;

import com.mineboundteam.minebound.MineBound;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class MagicSync {
    public static final SimpleChannel NET_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MineBound.MOD_ID, "magic_sync"),
            () -> "V1", a -> a.equals("V1"), a -> a.equals("V1"));

    public static void registerPackets(FMLCommonSetupEvent event) {
        NET_CHANNEL.registerMessage(0, MagicButtonSync.ButtonMsg.class, MagicButtonSync.ButtonMsg::encode, MagicButtonSync.ButtonMsg::decode,
                MagicButtonSync::handleButtons);
        NET_CHANNEL.registerMessage(1, MagicButtonSync.ButtonHeldMsg.class, MagicButtonSync.ButtonHeldMsg::encode, MagicButtonSync.ButtonHeldMsg::decode, MagicButtonSync::handleButtonHeld);
        NET_CHANNEL.registerMessage(2, MagicAnimationSync.ArmUsersMsg.class, MagicAnimationSync.ArmUsersMsg::encode, MagicAnimationSync.ArmUsersMsg::decode, MagicAnimationSync::handleArmUsers);
    }
}
