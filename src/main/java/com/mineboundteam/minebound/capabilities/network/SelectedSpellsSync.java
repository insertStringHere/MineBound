package com.mineboundteam.minebound.capabilities.network;

import java.util.function.Supplier;

import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.NetworkEvent;

public class SelectedSpellsSync {
    private int slot;
    private int index;
    private boolean isPrimary;

    public SelectedSpellsSync(boolean isPrimary, EquipmentSlot pSlot, int pIndex) {
        if(pSlot == null)
            this.slot = -1;
        else
            this.slot = pSlot.getIndex();
        this.index = pIndex;
        this.isPrimary = isPrimary;
    }

    private SelectedSpellsSync() {
    }

    public EquipmentSlot getSlot() {
        if (slot == -1)
            return null;
        return EquipmentSlot.byTypeAndIndex(Type.ARMOR, slot);
    }

    public int getIndex() {
        return index;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public static void encode(SelectedSpellsSync msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isPrimary);
        buf.writeInt(msg.index);
        buf.writeInt(msg.slot);
    }

    public static SelectedSpellsSync decode(FriendlyByteBuf buf) {
        SelectedSpellsSync msg = new SelectedSpellsSync();
        msg.isPrimary = buf.readBoolean();
        msg.index = buf.readInt();
        msg.slot = buf.readInt();
        return msg;
    }

    @SuppressWarnings("resource")
    public static void handleSelectedSpells(SelectedSpellsSync msg, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().player.getCapability((Capability<? extends SelectedSpell>)(msg.isPrimary() ? PlayerSelectedSpellsProvider.PRIMARY_SPELL
                 : PlayerSelectedSpellsProvider.SECONDARY_SPELL)).ifPresent(spell -> {
                     spell.equippedSlot = msg.getSlot();
                     spell.index = msg.getIndex();
                 });
     }
}