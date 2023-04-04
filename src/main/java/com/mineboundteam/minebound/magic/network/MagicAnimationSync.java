package com.mineboundteam.minebound.magic.network;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.mineboundteam.minebound.magic.MagicEvents;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class MagicAnimationSync {

    // TODO: currently the set of animations are updated in the key press and release, which seems to lose track of state. A possible alternative would be to have client keep track of local keydowns and sync them with the server directly when there's a change, but this would be more network intensive than the server trying to solve itself. 
    public static class ArmUsersMsg {
        public ConcurrentHashMap<Integer, Integer> players; 
    
        public static final int PRIMARY = 1;
        public static final int SECONDARY = 2;
    
        public ArmUsersMsg(ConcurrentHashMap<Integer, Integer> pHashMap){
            this.players = pHashMap;
        }
    
        public static void encode(ArmUsersMsg msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.players.keySet().size());
            for(Integer key : msg.players.keySet()){
                buf.writeInt(key);
                buf.writeInt(msg.players.get(key));
            }
        }
    
        public static ArmUsersMsg decode(FriendlyByteBuf buf) {
            int size = buf.readInt();
            HashMap<Integer, Integer> players = new HashMap<>(size);
            for(int i = 0; i < size; i++){
                players.put(buf.readInt(), buf.readInt());
            }
    
            return new ArmUsersMsg(new ConcurrentHashMap<Integer, Integer>(players));
        }
    }

    public static void handleArmUsers(ArmUsersMsg msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> MagicEvents.playerStates = msg.players);
        ctx.get().setPacketHandled(true);
    }
    
}
