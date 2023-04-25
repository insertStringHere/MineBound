package com.mineboundteam.minebound.capabilities.network;

import java.util.function.Supplier;

import com.mineboundteam.minebound.capabilities.PlayerManaProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class ManaSync {
    private int currentMana;
    private int totalManaCap;
    private int availableManaCap;

    public ManaSync(int currentMana, int totalManaCap, int availableManaCap){
        this.currentMana = currentMana;
        this.totalManaCap = totalManaCap;
        this.availableManaCap = availableManaCap;
    }

    public int getAvailableManaCap() {
        return availableManaCap;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getTotalManaCap() {
        return totalManaCap;
    }

    public static void encode(ManaSync msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.currentMana);
        buf.writeInt(msg.totalManaCap);
        buf.writeInt(msg.availableManaCap);
    }

    public static ManaSync decode(FriendlyByteBuf buf) {
        return new ManaSync(buf.readInt(), buf.readInt(), buf.readInt());
    }

    
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("resource")
    public static void handleMana(ManaSync msg, Supplier<NetworkEvent.Context> ctx) {
        PlayerManaProvider.PlayerMana mana = Minecraft.getInstance().player.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
        if (mana == null) return;

        mana.setMana(msg.getCurrentMana());
        mana.setTotalManaCap(msg.getTotalManaCap());
        mana.setAvailableManaCap(msg.getAvailableManaCap());
    }
}