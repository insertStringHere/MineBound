package com.mineboundteam.minebound.container;

import com.mineboundteam.minebound.block.BlockRegistry;
import com.mineboundteam.minebound.blockentity.SpellHolderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SpellHolderContainer extends AbstractContainerMenu {
    private final ContainerData containerData;
    private final ContainerLevelAccess containerLevelAccess;

    protected SpellHolderContainer(int id, Inventory inventory, IItemHandler iItemHandler, BlockPos blockPos, ContainerData containerData) {
        super(ContainerRegistry.SPELL_HOLDER_CONTAINER.get(), id);
        this.containerData = containerData;
        containerLevelAccess = ContainerLevelAccess.create(inventory.player.getLevel(), blockPos);

        // making chest slots - michael
        for (int i = 0; i < 69; i++) addSlot(new SlotItemHandler(iItemHandler, i, 20 + i * 60, 20));

        // making inventory slots - michael
        int chestY = 18;
        int startX = 8;
        int startY = 84;
        int hotBarY = 142;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9 + 9, startX + j * chestY, startY + i * chestY));
            }
        }

        // making hot bar slots - michael
        for (int j = 0; j < 9; ++j) {
            addSlot(new Slot(inventory, j, startX + j * chestY, hotBarY));
        }

        this.addDataSlots(containerData);
    }

    public static @NotNull SpellHolderContainer getClientContainer(int id, Inventory inventory) {
        return new SpellHolderContainer(id, inventory, new ItemStackHandler(69), BlockPos.ZERO, new SimpleContainerData(1));
    }

    public static MenuConstructor getServerContainer(SpellHolderBlockEntity spellHolderBlockEntity, BlockPos blockPos) {
        return (id, inventory, player) -> new SpellHolderContainer(id, inventory, spellHolderBlockEntity.getItemStackHandler(), blockPos, spellHolderBlockEntity.getContainerData());
    }

    // the following code is stolen from i forgot - michael
    private static final int HOT_BAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOT_BAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 69;
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.containerLevelAccess, player, BlockRegistry.SPELL_HOLDER.get());
    }
}
