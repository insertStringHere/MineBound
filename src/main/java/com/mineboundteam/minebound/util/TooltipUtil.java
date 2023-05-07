package com.mineboundteam.minebound.util;

import com.mineboundteam.minebound.MineBound;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.*;

import java.util.List;

public class TooltipUtil {
    public static MutableComponent enabled = new TextComponent("ENABLED").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN);
    public static MutableComponent disabled = new TextComponent("DISABLED").withStyle(ChatFormatting.BOLD, ChatFormatting.RED);
    public static MutableComponent enabledHeader = new TextComponent("  Only while ").withStyle(ColorUtil.Tooltip.defaultColor)
            .append(enabled).append(":");

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
                .append("]").withStyle(Style.EMPTY.withBold(false));
    }

    public static TranslatableComponent level(int level) {
        return new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + level);
    }

    public static MutableComponent manaCost(Number manaCost, String description) {
        return new TextComponent("Costs ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(StringUtil.formatDecimal(manaCost) + " Mana").withStyle(ColorUtil.Tooltip.manaColorStyle))
                .append(description);
    }

    public static MutableComponent manaReduction(Number manaReduction) {
        return new TextComponent("Reduces ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("Manapool").withStyle(ColorUtil.Tooltip.manaColorStyle))
                .append(" by ")
                .append(new TextComponent(StringUtil.formatDecimal(manaReduction)).withStyle(ColorUtil.Tooltip.reductionColorStyle));
    }
}
