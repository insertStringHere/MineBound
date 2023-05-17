package com.mineboundteam.minebound.magic.network;

import com.mineboundteam.minebound.magic.MagicEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class MagicAnimationSync {

    public static class ArmUsersMsg {
        public ConcurrentHashMap<Integer, Integer> players;

        public static final int PRIMARY = 1;
        public static final int SECONDARY = 2;

        public ArmUsersMsg(ConcurrentHashMap<Integer, Integer> pHashMap) {
            this.players = pHashMap;
        }

        public static void encode(ArmUsersMsg msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.players.keySet().size());
            for (Integer key : msg.players.keySet()) {
                buf.writeInt(key);
                buf.writeInt(msg.players.get(key));
            }
        }

        public static ArmUsersMsg decode(FriendlyByteBuf buf) {
            int size = buf.readInt();
            HashMap<Integer, Integer> players = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                players.put(buf.readInt(), buf.readInt());
            }

            return new ArmUsersMsg(new ConcurrentHashMap<>(players));
        }
    }

    public static void handleArmUsers(ArmUsersMsg msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> MagicEvents.playerStates = msg.players);
        ctx.get().setPacketHandled(true);
    }

}
