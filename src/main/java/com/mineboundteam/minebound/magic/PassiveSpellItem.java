package com.mineboundteam.minebound.magic;

import java.util.List;

import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class PassiveSpellItem extends SpellItem {
    public PassiveSpellItem(Properties properties, SpellLevel level) {
        super(properties, level);
    }

    protected <T extends PassiveSpellItem> List<ItemStack> getEquippedSpellsOfType(Class<T> type, Player player) {
        NonNullList<ItemStack> spells = NonNullList.create();
        for (EquipmentSlot e : EquipmentSlot.values())
            player.getItemBySlot(e).getCapability(ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS).ifPresent(slots -> {
                for (ItemStack item : slots.items) {
                    if (type.isInstance(item))
                        spells.add(item);
                }
            });

        return spells;
    }
}
