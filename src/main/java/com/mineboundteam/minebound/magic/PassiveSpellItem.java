package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PassiveSpellItem extends SpellItem {
    protected MutableComponent enabled = new TextComponent("ENABLED").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN);
    protected MutableComponent disabled = new TextComponent("DISABLED").withStyle(ChatFormatting.BOLD, ChatFormatting.RED);
    protected MutableComponent enabledHeader = new TextComponent("  Only while ").withStyle(defaultColor)
            .append(enabled).append(":");

    public PassiveSpellItem(Properties properties, ArmorTier level, MagicType magicType, SpellType spellType) {
        super(properties, level, magicType, spellType);
    }

    // These functions could potentially be running every tick, so they should be as performant as possible.
    // Streams can help with readability, but they are massively slower than a for each loop (2 - 6 times slower in my testing).

    public static <T extends PassiveSpellItem> List<ItemStack> getEquippedSpellsOfType(Class<T> type, Player player) {
        NonNullList<ItemStack> spells = NonNullList.create();
        for (EquipmentSlot e : EquipmentSlot.values())
            if (e.getType() == EquipmentSlot.Type.ARMOR && player.getItemBySlot(e).getItem() instanceof MyrialArmorItem)
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
    public static <T extends PassiveSpellItem> List<T> getEquippedSpellItemsOfType(Class<T> type, Player player) {
        NonNullList<T> spells = NonNullList.create();
        for (EquipmentSlot e : EquipmentSlot.values())
            if (e.getType() == EquipmentSlot.Type.ARMOR && player.getItemBySlot(e).getItem() instanceof MyrialArmorItem)
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
    public static <T extends PassiveSpellItem> ItemStack getHighestEquippedSpellOfType(Class<T> type,
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

    @SuppressWarnings("unchecked")
    public static <T extends PassiveSpellItem> T getHighestSpellItem(Class<T> type, Player player) {
        T highestSpell = null;
        for (EquipmentSlot e : EquipmentSlot.values())
            if (e.getType() == EquipmentSlot.Type.ARMOR && player.getItemBySlot(e).getItem() instanceof MyrialArmorItem)
                for (Tag tag : ArmorNBTHelper.getSpellTag(player.getItemBySlot(e), ArmorNBTHelper.PASSIVE_SPELL)) {
                    if (tag instanceof CompoundTag cTag) {
                        ItemStack item = ItemStack.of(cTag);
                        if (type.isInstance(item.getItem()) && (highestSpell == null || ((T) item.getItem()).level.getValue() > highestSpell.level.getValue()))
                            highestSpell = (T) item.getItem();
                    }
                }
        return highestSpell;
    }

    public static <T extends PassiveSpellItem> T getHighestSpellItem(List<T> spells) {
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

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("While equipped in a ").withStyle(defaultColor)
                .append(new TextComponent("utility slot").withStyle(utilityColor))
                .append(":"));
    }

    protected void appendToggleTooltip(List<Component> pTooltipComponents, KeyMapping keybind, boolean predicate) {
        // Spacing
        pTooltipComponents.add(new TextComponent(" "));

        if (predicate) {
            pTooltipComponents.add(enabled.copy().append(getKey(keybind)));
        } else {
            pTooltipComponents.add(disabled.copy().append(getKey(keybind)));
        }
    }

    private MutableComponent getKey(KeyMapping keybind) {
        return new TextComponent(" [").withStyle(defaultColor)
                .append(keybind.getKey().getValue() == -1 ?
                        new TextComponent("unbound") :
                        new TextComponent("").withStyle(ChatFormatting.WHITE).append(keybind.getTranslatedKeyMessage()))
                .append("]");
    }
}
