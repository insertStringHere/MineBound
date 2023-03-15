package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public abstract class PassiveSpellItem extends SpellItem {
    public PassiveSpellItem(Properties properties, ArmorTier level) {
        super(properties, level);
    }

    // These functions could potentially be running every tick, so they should be as performant as possible.
    // Streams can help with readability, but they are massively slower than a for each loop (2 - 6 times slower in my testing).

    protected static <T extends PassiveSpellItem> List<ItemStack> getEquippedSpellsOfType(Class<T> type, Player player) {
        NonNullList<ItemStack> spells = NonNullList.create();
        for (EquipmentSlot e : EquipmentSlot.values())
            if(e.getType() == EquipmentSlot.Type.ARMOR && player.getItemBySlot(e).getItem() instanceof MyrialArmorItem)
                for (Tag tag : ArmorNBTHelper.getSpellTag(player.getItemBySlot(e), ArmorNBTHelper.PASSIVE_SPELL)) {
                    if (tag instanceof CompoundTag cTag) {
                        ItemStack item = ItemStack.of(cTag);
                        if (type.isInstance(item.getItem()))
                            spells.add(item);
                    }
                }
        return spells;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends PassiveSpellItem> List<T> getEquippedSpellItemsOfType(Class<T> type, Player player) {
        NonNullList<T> spells = NonNullList.create();
        for (EquipmentSlot e : EquipmentSlot.values())
            if(e.getType() == EquipmentSlot.Type.ARMOR && player.getItemBySlot(e).getItem() instanceof MyrialArmorItem)
                for (Tag tag : ArmorNBTHelper.getSpellTag(player.getItemBySlot(e), ArmorNBTHelper.PASSIVE_SPELL)) {
                    if (tag instanceof CompoundTag cTag) {
                        ItemStack item = ItemStack.of(cTag);
                        if (type.isInstance(item.getItem()))
                            spells.add((T) item.getItem());
                    }
                }
        return spells;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends PassiveSpellItem> ItemStack getHighestEquippedSpellOfType(Class<T> type,
            Player player) {
        List<ItemStack> spells = getEquippedSpellsOfType(type, player);
        ItemStack highestSpell = null;
        for (ItemStack spell : spells) {
            if (highestSpell == null) {
                highestSpell = spell;
            } else if (((T) spell.getItem()).level.getValue() > ((T) highestSpell.getItem()).level.getValue()) {
                highestSpell = spell;
            }
        }
        return highestSpell;
    }

    protected static <T extends PassiveSpellItem> T getHighestSpellItem(List<T> spells) {
        T highestSpell = null;
        for (T spell : spells) {
            if (highestSpell == null) {
                highestSpell = spell;
            } else if (spell.level.getValue() > highestSpell.level.getValue()) {
                highestSpell = spell;
            }
        }
        return highestSpell;
    }
}
