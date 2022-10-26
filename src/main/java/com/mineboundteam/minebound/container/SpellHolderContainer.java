package com.mineboundteam.minebound.container;

import com.mineboundteam.minebound.blockentity.SpellHolderBlockEntity;
import com.mineboundteam.minebound.registry.BlockRegistry;
import com.mineboundteam.minebound.registry.ContainerRegistry;
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

        makeChestSlots(inventory, iItemHandler);
        makeInventorySlots(inventory, iItemHandler);
        makeHotBarSlots(inventory, iItemHandler);
        this.addDataSlots(containerData);
    }

    private void makeChestSlots(Inventory inventory, IItemHandler iItemHandler) {
        for (int i = 0; i < 69; i++) addSlot(new SlotItemHandler(iItemHandler, i, 20 + i * 60, 20));
    }

    private void makeInventorySlots(Inventory inventory, IItemHandler iItemHandler) {
        int chestY = 18;
        int startX = 8;
        int startY = 84;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9 + 9, startX + j * chestY, startY + i * chestY));
            }
        }
    }

    private void makeHotBarSlots(Inventory inventory, IItemHandler iItemHandler) {
        int chestY = 18;
        int startX = 8;
        int hotBarY = 142;
        for (int j = 0; j < 9; ++j) {
            addSlot(new Slot(inventory, j, startX + j * chestY, hotBarY));
        }
    }

    public static @NotNull SpellHolderContainer getClientContainer(int id, Inventory inventory) {
        return new SpellHolderContainer(id, inventory, new ItemStackHandler(69), BlockPos.ZERO, new SimpleContainerData(1));
    }

    public static MenuConstructor getServerContainer(SpellHolderBlockEntity spellHolderBlockEntity, BlockPos blockPos) {
        return (id, inventory, player) -> new SpellHolderContainer(id, inventory, spellHolderBlockEntity.getItemStackHandler(), blockPos, spellHolderBlockEntity.getContainerData());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        // this is a stolen algorithm - michael
        int hotBarSlotCount = 9;
        int playerInventoryRowCount = 3;
        int playerInventoryColumnCount = 9;
        int playInventorySlotCount = playerInventoryColumnCount * playerInventoryRowCount;
        int vanillaSlotCount = hotBarSlotCount + playInventorySlotCount;
        int vanillaFirstSlotIndex = 0;
        int teInventoryFirstSlotIndex = vanillaFirstSlotIndex + vanillaSlotCount;
        int teInventorySlotCount = 69;
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (index < vanillaFirstSlotIndex + vanillaSlotCount) {
            if (!moveItemStackTo(sourceStack, teInventoryFirstSlotIndex, teInventoryFirstSlotIndex + teInventorySlotCount, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < teInventoryFirstSlotIndex + teInventorySlotCount) {
            if (!moveItemStackTo(sourceStack, vanillaFirstSlotIndex, vanillaFirstSlotIndex + vanillaSlotCount, false)) {
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
