package com.mineboundteam.minebound.container;

import com.mineboundteam.minebound.block.AlloyFurnaceBlockEntity;
import com.mineboundteam.minebound.registry.BlockRegistry;
import com.mineboundteam.minebound.registry.ContainerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AlloyFurnaceContainer extends AbstractContainerMenu {
    public final AlloyFurnaceBlockEntity alloyFurnaceBlockEntity;
    private final ContainerData containerData;
    private final Level level;

    public AlloyFurnaceContainer(int id, Inventory inventory, BlockEntity blockEntity, ContainerData containerData) {
        super(ContainerRegistry.ALLOY_FURNACE_CONTAINER.get(), id);
        checkContainerSize(inventory, AlloyFurnaceBlockEntity.size);
        alloyFurnaceBlockEntity = (AlloyFurnaceBlockEntity) blockEntity;
        level = inventory.player.level;
        this.containerData = containerData;

        addInventory(inventory);
        addHotBar(inventory);
        alloyFurnaceBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 21, 36));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 45, 36));
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 69, 36));
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 139, 36));
        });
        addDataSlots(containerData);
    }

    public AlloyFurnaceContainer(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(id, inventory, inventory.player.level.getBlockEntity(friendlyByteBuf.readBlockPos()), new SimpleContainerData(AlloyFurnaceBlockEntity.containerDataCount));
    }

    private void addInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
    }

    private void addHotBar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    public double getProgressPercent() {
        return (double) containerData.get(0) / AlloyFurnaceBlockEntity.maxProgress;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        // This code is from https://github.com/diesieben07/SevenCommons            - michael tran 11/25/2022
        int vanillaSlotCount = 36;
        int vanillaIndex = 0;
        int teInventoryIndex = vanillaIndex + vanillaSlotCount;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = slot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (index < vanillaIndex + vanillaSlotCount) {
            if (!moveItemStackTo(sourceStack, teInventoryIndex, teInventoryIndex
                    + AlloyFurnaceBlockEntity.size, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < teInventoryIndex + AlloyFurnaceBlockEntity.size) {
            if (!moveItemStackTo(sourceStack, vanillaIndex, vanillaIndex + vanillaSlotCount, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        slot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, alloyFurnaceBlockEntity.getBlockPos()), player, BlockRegistry.ALLOY_FURNACE.get());
    }
}
