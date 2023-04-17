package com.mineboundteam.minebound.magic.helper;

import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;
import com.mineboundteam.minebound.magic.ActiveSpellItem;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

public class UseSpellHelper {
    public static void useSpell(Player player, Capability<? extends SelectedSpell> cap) {
        player.getCapability(cap).ifPresent(selected -> {
            if (!selected.isEmpty()) {
                ItemStack activeSpell = ItemStack.of(ArmorNBTHelper.getSpellTag(player.getItemBySlot(selected.equippedSlot), ArmorNBTHelper.ACTIVE_SPELL).getCompound(selected.index));
                if (activeSpell.getItem() instanceof ActiveSpellItem spell) {
                    spell.use(activeSpell, player.level, player);
                }
            }
        });
    }

    public static void useSpellTick(Player player, Capability<? extends SelectedSpell> cap, int count) {
        player.getCapability(cap).ifPresent(selected -> {
            if (!selected.isEmpty()) {
                ItemStack activeSpell = ItemStack.of(ArmorNBTHelper.getSpellTag(player.getItemBySlot(selected.equippedSlot), ArmorNBTHelper.ACTIVE_SPELL).getCompound(selected.index));
                if (activeSpell.getItem() instanceof ActiveSpellItem spell) {
                    spell.onUsingTick(activeSpell,player.level,player);
                }
            }
        });
    }

    public static void releaseUsingSpell(Player player, Capability<? extends SelectedSpell> cap) {
        player.getCapability(cap).ifPresent(selected -> {
            if (!selected.isEmpty()) {
                ItemStack activeSpell = ItemStack.of(ArmorNBTHelper.getSpellTag(player.getItemBySlot(selected.equippedSlot), ArmorNBTHelper.ACTIVE_SPELL).getCompound(selected.index));
                if (activeSpell.getItem() instanceof ActiveSpellItem spell) {
                    spell.releaseUsing(activeSpell, player.level, player);
                }
            }
        });
    }
}
