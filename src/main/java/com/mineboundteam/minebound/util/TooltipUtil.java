package com.mineboundteam.minebound.util;

import com.mineboundteam.minebound.MineBound;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class TooltipUtil {
    public static MutableComponent enabled = new TextComponent("ENABLED").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN);
    public static MutableComponent disabled = new TextComponent("DISABLED").withStyle(ChatFormatting.BOLD, ChatFormatting.RED);
    public static MutableComponent enabledHeader = new TextComponent("  Only while ").withStyle(ColorUtil.Tooltip.defaultColor)
            .append(enabled).append(":");

    public static TranslatableComponent levelTooltip(int level) {
        return new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + level);
    }

    public static void appendToggleTooltip(List<Component> pTooltipComponents, KeyMapping keybind, boolean predicate) {
        // Spacing
        pTooltipComponents.add(new TextComponent(" "));

        if (predicate) {
            pTooltipComponents.add(enabled.copy().append(getKey(keybind)));
        } else {
            pTooltipComponents.add(disabled.copy().append(getKey(keybind)));
        }
    }

    public static MutableComponent getKey(KeyMapping keybind) {
        return new TextComponent(" [").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(keybind.getKey() == InputConstants.UNKNOWN ?
                        new TextComponent("unbound") :
                        new TextComponent("").withStyle(ChatFormatting.WHITE).append(keybind.getTranslatedKeyMessage()))
                .append("]");
    }
}
