package com.mineboundteam.minebound.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MyrialSwordVacuum extends Item {
    public ItemStack itemStack;

    public MyrialSwordVacuum(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, Level level, @NotNull Entity entity, int slotID, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player && (!isSelected || player.containerMenu != player.inventoryMenu)) {
            this.itemStack.getOrCreateTag().putBoolean("minebound.return_myrial_sword", true);
            player.getInventory().removeItem(itemStack);
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack itemStack, Player player) {
        this.itemStack.getOrCreateTag().putBoolean("minebound.return_myrial_sword", true);
        player.getInventory().removeItem(itemStack);
        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!level.isClientSide && itemStack != null)
            itemStack.getOrCreateTag().putBoolean("minebound.return_myrial_sword", true);
        return super.use(level, player, interactionHand);
    }
}
