package com.mineboundteam.minebound.magic.network;

import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.inventory.SelectSpellMenu;
import com.mineboundteam.minebound.inventory.containers.InventorySpellContainer;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.MagicEvents;
import com.mineboundteam.minebound.magic.helper.UseSpellHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MagicButtonSync {

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
                        if (canOpen && chest.getItem() instanceof MyrialArmorItem item && item.getTier().handSlots > 0)
                            player.openMenu(new SimpleMenuProvider((containerID, inventory, p) -> new SelectSpellMenu(containerID, inventory, true), TextComponent.EMPTY));
                    }
                    case PRIMARY_PRESSED -> {
                        if (chest.getItem() instanceof MyrialArmorItem item && item.getTier().handSlots > 0) {
                            UseSpellHelper.useSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL);

                            int handStates = MagicEvents.playerStates.getOrDefault(player.getId(), 0);
                            handStates |= MagicAnimationSync.ArmUsersMsg.PRIMARY;
                            MagicEvents.playerStates.put(player.getId(), handStates);
                        }
                    }
                    case PRIMARY_RELEASED -> {
                        if (chest.getItem() instanceof MyrialArmorItem item && item.getTier().handSlots > 0)
                            UseSpellHelper.releaseUsingSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL);

                        int handStates = MagicEvents.playerStates.getOrDefault(player.getId(), 0);
                        handStates &= ~MagicAnimationSync.ArmUsersMsg.PRIMARY;
                        if (handStates != 0)
                            MagicEvents.playerStates.put(player.getId(), handStates);
                        else
                            MagicEvents.playerStates.remove(player.getId());
                    }
                    case SECONDARY_MENU -> {
                        if (canOpen && chest.getItem() instanceof MyrialArmorItem item && item.getTier().handSlots > 1)
                            player.openMenu(new SimpleMenuProvider((containerID, inventory, p) -> new SelectSpellMenu(containerID, inventory, false), TextComponent.EMPTY));
                    }
                    case SECONDARY_PRESSED -> {
                        if (chest.getItem() instanceof MyrialArmorItem item && item.getTier().handSlots > 1) {
                            UseSpellHelper.useSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL);

                            int handStates = MagicEvents.playerStates.getOrDefault(player.getId(), 0);
                            handStates |= MagicAnimationSync.ArmUsersMsg.SECONDARY;
                            MagicEvents.playerStates.put(player.getId(), handStates);
                        }
                    }
                    case SECONDARY_RELEASED -> {
                        if (chest.getItem() instanceof MyrialArmorItem item && item.getTier().handSlots > 1)
                            UseSpellHelper.releaseUsingSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL);
                        
                        int handStates = MagicEvents.playerStates.getOrDefault(player.getId(), 0);
                        handStates &= ~MagicAnimationSync.ArmUsersMsg.SECONDARY;
                        if (handStates != 0)
                            MagicEvents.playerStates.put(player.getId(), handStates);
                        else
                            MagicEvents.playerStates.remove(player.getId());
                    }
                    case FIRE_UTILITY_TOGGLE ->
                            player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(toggle -> toggle.fire = !toggle.fire);
                    case EARTH_UTILITY_TOGGLE ->
                            player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(toggle -> toggle.earth = !toggle.earth);
                    case LIGHT_UTILITY_TOGGLE ->
                            player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(toggle -> toggle.light = !toggle.light);
                    case ENDER_UTILITY_TOGGLE ->
                            player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(toggle -> toggle.ender = !toggle.ender);
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
                        if (chest.getItem() instanceof MyrialArmorItem item && item.getTier().handSlots > 0)
                            UseSpellHelper.useSpellTick(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL, msg.holdTicks);
                    }
                    case SECONDARY -> {
                        if (chest.getItem() instanceof MyrialArmorItem item && item.getTier().handSlots > 1)
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

        public enum MsgType {
            PRIMARY_PRESSED(0),
            PRIMARY_RELEASED(1),
            SECONDARY_PRESSED(2),
            SECONDARY_RELEASED(3),
            PRIMARY_MENU(4),
            SECONDARY_MENU(5),
            FIRE_UTILITY_TOGGLE(6),
            EARTH_UTILITY_TOGGLE(7),
            LIGHT_UTILITY_TOGGLE(8),
            ENDER_UTILITY_TOGGLE(9);
            private final int msgVal;

            MsgType(int val) {
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

    public static class ButtonHeldMsg {
        public MsgType hand;
        public int holdTicks;

        public enum MsgType {
            PRIMARY(0),
            SECONDARY(1);
            private final int msgVal;

            MsgType(int val) {
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

        public ButtonHeldMsg(MsgType h, int count) {
            hand = h;
            holdTicks = count;
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
