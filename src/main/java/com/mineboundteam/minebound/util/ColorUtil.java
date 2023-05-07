package com.mineboundteam.minebound.util;

import com.mineboundteam.minebound.item.armor.ArmorTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffect;

import java.util.HashMap;

public class ColorUtil {
    public static final int MANA_COLOR = (77 << 16) + (106 << 8) + (255);
    public static final int REDUCTION_COLOR = 0xF6F116;

    public static class Tooltip {
        public static final HashMap<ArmorTier, ChatFormatting> armorTierColors = new HashMap<>() {{
            put(ArmorTier.EFFIGY, ChatFormatting.YELLOW);
            put(ArmorTier.SUIT, ChatFormatting.GOLD);
            put(ArmorTier.SYNERGY, ChatFormatting.DARK_AQUA);
            put(ArmorTier.SINGULARITY, ChatFormatting.WHITE);
        }};

        public static final Style manaColorStyle = Style.EMPTY.withColor(MANA_COLOR);
        public static final Style reductionColorStyle = Style.EMPTY.withColor(REDUCTION_COLOR);
        public static final ChatFormatting defaultColor = ChatFormatting.GRAY;
        public static final ChatFormatting utilityColor = ChatFormatting.DARK_PURPLE;
        public static final ChatFormatting damageColor = ChatFormatting.RED;
        public static final ChatFormatting timeAndDistanceColor = ChatFormatting.DARK_GREEN;
        public static final ChatFormatting enchantmentColor = ChatFormatting.AQUA;
        public static final ChatFormatting itemColor = ChatFormatting.WHITE;

        public static Style effectColor(MobEffect effect) {
            return Style.EMPTY.withColor(effect.getColor());
        }
    }
}
