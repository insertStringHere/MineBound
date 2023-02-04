package com.mineboundteam.minebound.inventory.slots;

import com.mineboundteam.minebound.registry.RecipeRegistry;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

public class OutputArmorSlot extends ResultSlot {
    protected final CraftingContainer craftSlots;
    protected final Player player;

    public OutputArmorSlot(Player player, CraftingContainer craftingContainer, Container resultContainer,
            int slotIndex, int x, int y) {
        super(player, craftingContainer, resultContainer, slotIndex, x, y);
        craftSlots = craftingContainer;
        this.player = player;
    }

    public void onTake(Player pPlayer, ItemStack pStack) {
        if(pPlayer.level.isClientSide) return;
        this.checkTakeAchievements(pStack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(pPlayer);
        NonNullList<ItemStack> nonnulllist = pPlayer.level.getRecipeManager()
                .getRemainingItemsFor(RecipeRegistry.ARMOR_FORGE_RECIPE.get(), this.craftSlots, pPlayer.level);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = this.craftSlots.getItem(i);
            ItemStack itemstack1 = nonnulllist.get(i);
            if (!itemstack.isEmpty()) {
                this.craftSlots.removeItem(i, 1);
                itemstack = this.craftSlots.getItem(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.craftSlots.setItem(i, itemstack1);
                } else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.craftSlots.setItem(i, itemstack1);
                } else if (this.player.getInventory().add(itemstack1)) {
                    this.player.drop(itemstack1, false);
                }
            }
        }

    }
}