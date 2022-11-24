package com.mineboundteam.minebound.blockentity;

import com.mineboundteam.minebound.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AlloyFurnaceBlockEntity extends BlockEntity implements MenuProvider {
    private final ContainerData containerData;
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(size) {
        @Override
        protected void onContentsChanged(int slots) {
            setChanged();
        }
    };
    private LazyOptional<IItemHandler> lazyOptional = LazyOptional.empty();
    private int progress = 0;
    private final int size = 4;

    public AlloyFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.ALLOY_FURNACE_BLOCK_ENTITY.get(), blockPos, blockState);
        containerData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                }
            }

            @Override
            public int getCount() {
                return size;
            }
        };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return null;
    }

    public void dropContents() {
        SimpleContainer simpleContainer = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            simpleContainer.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        if (level != null) {
            Containers.dropContents(level, worldPosition, simpleContainer);
        }
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction direction) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyOptional.cast() : super.getCapability(capability, direction);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.nullToEmpty("Alloy Furnace");
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyOptional.invalidate();
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        itemStackHandler.deserializeNBT(compoundTag.getCompound("itemStackHandler"));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyOptional = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        compoundTag.put("itemStackHandler", itemStackHandler.serializeNBT());
        super.saveAdditional(compoundTag);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AlloyFurnaceBlockEntity alloyFurnaceBlockEntity) {
        if (level.isClientSide()) {
            return;
        }

        if (canSmeltItem(alloyFurnaceBlockEntity)) {
            alloyFurnaceBlockEntity.progress++;
            setChanged(level, blockPos, blockState);

            if (alloyFurnaceBlockEntity.progress >= 78) {
                smeltItem(alloyFurnaceBlockEntity);
            }
        } else {
            alloyFurnaceBlockEntity.progress = 0;
            setChanged(level, blockPos, blockState);
        }
    }

    private static boolean canSmeltItem(AlloyFurnaceBlockEntity alloyFurnaceBlockEntity) {
        SimpleContainer simpleContainer = new SimpleContainer(alloyFurnaceBlockEntity.itemStackHandler.getSlots());
        for (int i = 0; i < alloyFurnaceBlockEntity.itemStackHandler.getSlots(); i++) {
            simpleContainer.setItem(i, alloyFurnaceBlockEntity.itemStackHandler.getStackInSlot(i));
        }

        boolean hasRawGemInFirstSlot = alloyFurnaceBlockEntity.itemStackHandler.getStackInSlot(1).getItem() == Items.COAL;
        boolean canInsertAmountIntoOutputSlot = simpleContainer.getItem(2).getMaxStackSize() > simpleContainer.getItem(2).getCount();
        boolean canInsertItemIntoOutputSlot = simpleContainer.getItem(2).getItem() == new ItemStack(Items.DIAMOND, 1).getItem() || simpleContainer.getItem(2).isEmpty();

        return hasRawGemInFirstSlot && canInsertAmountIntoOutputSlot && canInsertItemIntoOutputSlot;
    }

    private static void craftItem(GemInfusingStationBlockEntity pEntity) {

        if(hasRecipe(pEntity)) {
            pEntity.itemStackHandler.extractItem(1, 1, false);
            pEntity.itemStackHandler.setStackInSlot(2, new ItemStack(ModItems.ZIRCON.get(),
                    pEntity.itemStackHandler.getStackInSlot(2).getCount() + 1));

            pEntity.resetProgress();
        }
    }
//    public ContainerData getContainerData() {
//        return containerData;
//    }
//
//    public ItemStackHandler getItemStackHandler() {
//        return itemStackHandler;
//    }
}
