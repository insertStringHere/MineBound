package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class ActiveSpellItem extends SpellItem {

    public ActiveSpellItem(Properties properties, ArmorTier level) {
        super(properties, level);
    }

    @Override
    public abstract InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand);

    // These functions could potentially be running every tick, so they should be as performant as possible.
    // Streams can help with readability, but they are massively slower than a for each loop (2 - 6 times slower in my testing).

    protected static <T extends ActiveSpellItem> List<ItemStack> getEquippedSpellsOfType(Class<T> type, Player player) {
        return getEquippedSpellsOfType(type, player, ArmorSpellsProvider.ARMOR_ACTIVE_SPELLS);
    }

    protected static <T extends ActiveSpellItem> List<T> getEquippedSpellItemsOfType(Class<T> type, Player player) {
        return getEquippedSpellItemsOfType(type, player, ArmorSpellsProvider.ARMOR_ACTIVE_SPELLS);
    }

    protected static <T extends ActiveSpellItem> ItemStack getHighestEquippedSpellOfType(Class<T> type, Player player) {
        return getHighestEquippedSpellFromList(type, getEquippedSpellsOfType(type, player));
    }
}
