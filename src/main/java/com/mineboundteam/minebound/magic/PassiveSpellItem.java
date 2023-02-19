package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PassiveSpellItem extends SpellItem {
    public PassiveSpellItem(Properties properties, ArmorTier level) {
        super(properties, level);
    }

    // These functions could potentially be running every tick, so they should be as performant as possible.
    // Streams can help with readability, but they are massively slower than a for each loop (2 - 6 times slower in my testing).

    protected static <T extends PassiveSpellItem> List<ItemStack> getEquippedSpellsOfType(Class<T> type, Player player) {
        return getEquippedSpellsOfType(type, player, ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS);
    }

    protected static <T extends PassiveSpellItem> List<T> getEquippedSpellItemsOfType(Class<T> type, Player player) {
        return getEquippedSpellItemsOfType(type, player, ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS);
    }

    protected static <T extends PassiveSpellItem> ItemStack getHighestEquippedSpellOfType(Class<T> type, Player player) {
        return getHighestEquippedSpellFromList(type, getEquippedSpellsOfType(type, player));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While equipped in a ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("utility slot").withStyle(ChatFormatting.DARK_PURPLE))
                                       .append(":"));
    }
}
