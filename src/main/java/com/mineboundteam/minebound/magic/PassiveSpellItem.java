package com.mineboundteam.minebound.magic;

import java.util.List;

import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.item.armor.ArmorTier;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class PassiveSpellItem extends SpellItem {
    public PassiveSpellItem(Properties properties, ArmorTier level) {
        super(properties, level);
    }

    @SuppressWarnings("unchecked")
    protected static <T extends PassiveSpellItem> List<T> getEquippedSpellsOfType(Class<T> type, Player player) {
        NonNullList<T> spells = NonNullList.create();
        for (EquipmentSlot e : EquipmentSlot.values())
            player.getItemBySlot(e).getCapability(ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS).ifPresent(slots -> {
                for (ItemStack item : slots.items) {
                    if (type.isInstance(item.getItem()))
                        spells.add((T) item.getItem());
                }
            });

        return spells;
    }
}
