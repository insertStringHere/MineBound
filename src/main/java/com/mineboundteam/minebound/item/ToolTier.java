package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.registry.ItemRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ToolTier {
    public static final ForgeTier ENERGIZED_IRON = new ForgeTier(2, 250, 7, 2, 8, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(ItemRegistry.ENERGIZED_IRON_INGOT.get()));
    public static final ForgeTier ILLUMINANT_STEEL = new ForgeTier(2, 1561, 7.5F, 2.3F, 8, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(ItemRegistry.ILLUMINANT_STEEL_INGOT.get()));
    public static final ForgeTier GILDED_DIAMOND = new ForgeTier(3, 1700, 10.5F, 3, 16, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ItemRegistry.GILDED_DIAMOND_INGOT.get()));
}
