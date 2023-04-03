package com.mineboundteam.minebound.magic.network;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.inventory.SelectSpellMenu;
import com.mineboundteam.minebound.inventory.containers.InventorySpellContainer;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.helper.UseSpellHelper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class MagicSync {
    public static final SimpleChannel NET_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MineBound.MOD_ID, "magic_sync"),
            () -> "V1", a -> a.equals("V1"), a -> a.equals("V1"));

    public static void registerPackets(FMLCommonSetupEvent event) {
        NET_CHANNEL.registerMessage(0, ButtonMsg.class, ButtonMsg::encode, ButtonMsg::decode,
                MagicSync::handleButtons);
        NET_CHANNEL.registerMessage(1, ButtonHeldMsg.class, ButtonHeldMsg::encode, ButtonHeldMsg::decode, MagicSync::handleButtonHeld);
    }

    public static void handleButtons(ButtonMsg msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (!player.level.isClientSide()) {

                var activeSpells = new InventorySpellContainer(player.getInventory(), PlayerSelectedSpellsProvider.PRIMARY_SPELL, ArmorNBTHelper.ACTIVE_SPELL, true);
                var passiveSpells = new InventorySpellContainer(player.getInventory(), PlayerSelectedSpellsProvider.PRIMARY_SPELL, ArmorNBTHelper.PASSIVE_SPELL, true);
                boolean canOpen = !(activeSpells.isEmpty() && passiveSpells.isEmpty());

                ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
                switch (msg.msg) {
                    case PRIMARY_MENU -> {
                        if (canOpen && chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 0)
                            player.openMenu(new SimpleMenuProvider((containerID, inventory, p) -> new SelectSpellMenu(containerID, inventory, true), TextComponent.EMPTY));
                    }
                    case PRIMARY_PRESSED -> {
                        if (chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 0)
                            UseSpellHelper.useSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL);
                    }
                    case PRIMARY_RELEASED -> {
                        if (chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 0)
                            UseSpellHelper.releaseUsingSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL);
                    }
                    case SECONDARY_MENU -> {
                        if (canOpen && chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 1)
                            player.openMenu(new SimpleMenuProvider((containerID, inventory, p) -> new SelectSpellMenu(containerID, inventory, false), TextComponent.EMPTY));
                    }
                    case SECONDARY_PRESSED -> {
                        if (chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 1)
                            UseSpellHelper.useSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL);
                    }
                    case SECONDARY_RELEASED -> {
                        if (chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 1)
                            UseSpellHelper.releaseUsingSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL);
                    }
                    case FIRE_UTILITY_TOGGLE -> {
                        player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(toggle -> {
                            toggle.fire = !toggle.fire;
                        });
                    }
                    default -> {
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleButtonHeld(ButtonHeldMsg msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (!player.level.isClientSide()) {
                ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
                switch (msg.hand) {
                    case PRIMARY -> {
                        if (chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 0)
                            UseSpellHelper.useSpellTick(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL, msg.holdTicks);
                    }
                    case SECONDARY -> {
                        if (chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 1)
                            UseSpellHelper.useSpellTick(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL, msg.holdTicks);
                    }
                    default -> {
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }



    public static class ButtonMsg {
        public MsgType msg;

        public static enum MsgType {
            PRIMARY_PRESSED(0),
            PRIMARY_RELEASED(1),
            SECONDARY_PRESSED(2),
            SECONDARY_RELEASED(3),
            PRIMARY_MENU(4),
            SECONDARY_MENU(5),
            FIRE_UTILITY_TOGGLE(6);
            protected int msgVal;

            private MsgType(int val) {
                this.msgVal = val;
            }

            public int getVal() {
                return msgVal;
            }

            public static MsgType getFromVal(int val) {
                for (MsgType v : MsgType.values())
                    if (v.msgVal == val)
                        return v;
                return null;
            }
        }

        public ButtonMsg(MsgType msg) {
            this.msg = msg;
        }

        public static void encode(ButtonMsg msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.msg.getVal());
        }

        public static ButtonMsg decode(FriendlyByteBuf buf) {
            return new ButtonMsg(MsgType.getFromVal(buf.readInt()));
        }
    }

    public static class ButtonHeldMsg{
        public MsgType hand;
        public int holdTicks;

        public static enum MsgType{
            PRIMARY(0),
            SECONDARY(1);
            protected int msgVal;

            private MsgType(int val) {
                this.msgVal = val;
            }

            public int getVal() {
                return msgVal;
            }

            public static MsgType getFromVal(int val) {
                for (MsgType v : MsgType.values())
                    if (v.msgVal == val)
                        return v;
                return null;
            }
        }

        public ButtonHeldMsg(MsgType h, int count){
            hand = h;
            count = holdTicks;
        }

        public static void encode(ButtonHeldMsg msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.hand.getVal());
            buf.writeInt(msg.holdTicks);
        }

        public static ButtonHeldMsg decode(FriendlyByteBuf buf) {
            return new ButtonHeldMsg(MsgType.getFromVal(buf.readInt()), buf.readInt());
        }

    }
}
