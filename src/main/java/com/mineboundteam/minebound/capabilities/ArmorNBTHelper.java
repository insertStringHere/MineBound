package com.mineboundteam.minebound.capabilities;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class ArmorNBTHelper {
    public static final String ACTIVE_SPELL = "activeSpells";
    public static final String PASSIVE_SPELL = "passiveSpells";

    public static ListTag getSpellTag(ItemStack armorItem, String tagKey) {
        return armorItem.getOrCreateTagElement("spell").getList(tagKey, 10);
    }
    public static void saveSpellTag(ItemStack armorItem, String tagKey, ListTag tag){
        armorItem.getOrCreateTagElement("spell").put(tagKey, tag);
    }
}
