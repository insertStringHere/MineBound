package com.mineboundteam.minebound.capabilities.network;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider.SpellContainer;
import com.mineboundteam.minebound.inventory.ArmorForgeMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
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
        NET_CHANNEL.registerMessage(0, AllItemSync.class, AllItemSync::encode, AllItemSync::decode,
                (msg, ctx) -> clientHandle(msg, ctx, CapabilitySync::handleAllItems));
        NET_CHANNEL.registerMessage(1, SelectedSpellsSync.class, SelectedSpellsSync::encode, SelectedSpellsSync::decode,
                (msg, ctx) -> clientHandle(msg, ctx, CapabilitySync::handleSelectedSpells));
    }

    protected static <T> void clientHandle(T msg, Supplier<NetworkEvent.Context> ctx,
            BiConsumer<T, Supplier<NetworkEvent.Context>> func) {
        ctx.get().enqueueWork(() ->
        // Make sure it's only executed on the physical client
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> func.accept(msg, ctx)));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleAllItems(AllItemSync msg, Supplier<NetworkEvent.Context> ctx) {
        AbstractContainerMenu menu = Minecraft.getInstance().player.containerMenu;
        if (menu.containerId == msg.getContainer() && menu instanceof ArmorForgeMenu)
            ((ArmorForgeMenu) menu).slots.get(ArmorForgeMenu.ARMOR_INPUT_INDEX).getItem()
                    .getCapability(msg.getContainerType()).ifPresent(slots -> {
                        slots.items.clear();
                        for (ItemStack i : msg.items)
                            slots.items.add(i);
                    });
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleSelectedSpells(SelectedSpellsSync msg, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().player.getCapability(msg.isPrimary() ? PlayerSelectedSpellsProvider.PRIMARY_SPELL
                : PlayerSelectedSpellsProvider.SECONDARY_SPELL).ifPresent(spell -> {
                    spell.equippedSlot = msg.getSlot();
                    spell.index = msg.getIndex();
                });
    }

    public static class SelectedSpellsSync {
        private int slot;
        private int index;
        private boolean isPrimary;

        public SelectedSpellsSync(boolean isPrimary, EquipmentSlot pSlot, int pIndex) {
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

    public static class AllItemSync {
        private int containerIndex;
        private List<ItemStack> items;

        private int containerType = 0;

        public AllItemSync(int inventoryIndex, List<ItemStack> items) {
            this.items = items;
            this.containerIndex = inventoryIndex;
        }

        public List<ItemStack> getItems() {
            return items;
        }

        public int getContainer() {
            return containerIndex;
        }

        public AllItemSync setContainerType(Capability<SpellContainer> type) {
            if (type.equals(ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS))
                containerType = 1;
            return this;
        }

        public Capability<SpellContainer> getContainerType() {
            switch (containerType) {
                case 1:
                    return ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS;
            }

            return ArmorSpellsProvider.ARMOR_ACTIVE_SPELLS;
        }

        public static void encode(AllItemSync msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.containerIndex);
            buf.writeInt(msg.containerType);
            buf.writeInt(msg.items.size());
            for (ItemStack item : msg.items) {
                buf.writeItem(item);
            }
        }

        public static AllItemSync decode(FriendlyByteBuf buf) {
            AllItemSync itemSync = new AllItemSync(buf.readInt(), NonNullList.create());
            itemSync.containerType = buf.readInt();
            int count = buf.readInt();
            for (int i = 0; i < count; i++) {
                itemSync.items.add(buf.readItem());
            }
            return itemSync;
        }
    }
}
