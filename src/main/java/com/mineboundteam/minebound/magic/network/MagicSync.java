package com.mineboundteam.minebound.magic.network;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import com.mineboundteam.minebound.inventory.SelectSpellMenu;
import com.mineboundteam.minebound.inventory.containers.InventorySpellContainer;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.ActiveSpellItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
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
                            useSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL);
                    }
                    case PRIMARY_RELEASED -> {
                        if (chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 0)
                            releaseUsingSpell(player, PlayerSelectedSpellsProvider.PRIMARY_SPELL);
                    }
                    case SECONDARY_MENU -> {
                        if (canOpen && chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 1)
                            player.openMenu(new SimpleMenuProvider((containerID, inventory, p) -> new SelectSpellMenu(containerID, inventory, false), TextComponent.EMPTY));
                    }
                    case SECONDARY_PRESSED -> {
                        if (chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 1)
                            useSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL);
                    }
                    case SECONDARY_RELEASED -> {
                        if (chest.getItem() instanceof MyrialArmorItem && ((MyrialArmorItem) chest.getItem()).getTier().handSlots > 1)
                            releaseUsingSpell(player, PlayerSelectedSpellsProvider.SECONDARY_SPELL);
                    }
                    case FIRE_UTILITY_TOGGLE -> {
                        player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(toggle -> {
                            toggle.fire = !toggle.fire;
                        });
                    }
                    case EARTH_UTILITY_TOGGLE -> {
                        player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(toggle -> {
                            toggle.earth = !toggle.earth;
                        });
                    }
                    default -> {
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    protected static void useSpell(Player player, Capability<? extends SelectedSpell> cap) {
        player.getCapability(cap).ifPresent(selected -> {
            if (!selected.isEmpty()) {
                ItemStack activeSpell = ItemStack.of(ArmorNBTHelper.getSpellTag(player.getItemBySlot(selected.equippedSlot), ArmorNBTHelper.ACTIVE_SPELL).getCompound(selected.index));
                if (activeSpell.getItem() instanceof ActiveSpellItem spell) {
                    spell.use(activeSpell, player.level, player);
                }
            }
        });
    }

    protected static void releaseUsingSpell(Player player, Capability<? extends SelectedSpell> cap) {
        player.getCapability(cap).ifPresent(selected -> {
            if (!selected.isEmpty()) {
                ItemStack activeSpell = ItemStack.of(ArmorNBTHelper.getSpellTag(player.getItemBySlot(selected.equippedSlot), ArmorNBTHelper.ACTIVE_SPELL).getCompound(selected.index));
                if (activeSpell.getItem() instanceof ActiveSpellItem spell) {
                    spell.releaseUsing(activeSpell, player.level, player);
                }
            }
        });
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
            FIRE_UTILITY_TOGGLE(6),
            EARTH_UTILITY_TOGGLE(7);

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
}
