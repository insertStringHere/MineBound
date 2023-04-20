package com.mineboundteam.minebound.capabilities.network;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@SuppressWarnings("resource")
public class CapabilitySync {
    public static final SimpleChannel NET_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MineBound.MOD_ID, "capability_sync"),
            () -> "V1.1", a -> a.equals("V1.1"), a -> a.equals("V1.1"));
    public static long msgId = 0;

    public static void registerPackets(FMLCommonSetupEvent event) {
        NET_CHANNEL.registerMessage(1, SelectedSpellsSync.class, SelectedSpellsSync::encode, SelectedSpellsSync::decode,
                (msg, ctx) -> clientHandle(msg, ctx, CapabilitySync::handleSelectedSpells));
        NET_CHANNEL.registerMessage(2, ManaSync.class, ManaSync::encode, ManaSync::decode,
                (msg, ctx) -> clientHandle(msg, ctx, CapabilitySync::handleMana));
    }




    protected static <T> void clientHandle(T msg, Supplier<NetworkEvent.Context> ctx,
            BiConsumer<T, Supplier<NetworkEvent.Context>> func) {
        ctx.get().enqueueWork(() ->
        // Make sure it's only executed on the physical client
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> func.accept(msg, ctx)));
        ctx.get().setPacketHandled(true);
    }


    @OnlyIn(Dist.CLIENT)
    public static void handleMana(ManaSync msg, Supplier<NetworkEvent.Context> ctx) {
        PlayerManaProvider.PlayerMana mana = Minecraft.getInstance().player.getCapability(PlayerManaProvider.PLAYER_MANA).orElse(null);
        if (mana == null) return;

        mana.setMana(msg.getCurrentMana());
        mana.setTotalManaCap(msg.getTotalManaCap());
        mana.setAvailableManaCap(msg.getAvailableManaCap());
    }

    public static void handleSelectedSpells(SelectedSpellsSync msg, Supplier<NetworkEvent.Context> ctx) {
       Minecraft.getInstance().player.getCapability((Capability<? extends SelectedSpell>)(msg.isPrimary() ? PlayerSelectedSpellsProvider.PRIMARY_SPELL
                : PlayerSelectedSpellsProvider.SECONDARY_SPELL)).ifPresent(spell -> {
                    spell.equippedSlot = msg.getSlot();
                    spell.index = msg.getIndex();
                });
    }

    public static class SelectedSpellsSync {
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
    }

    public static class ManaSync {
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
    }
}
