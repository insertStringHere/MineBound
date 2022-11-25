package com.mineboundteam.minebound.blockentity;

import com.mineboundteam.minebound.container.AlloyFurnaceContainer;
import com.mineboundteam.minebound.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
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

public class AlloyFurnaceBlockEntity extends BlockEntity implements MenuProvider {
    private final ContainerData containerData;
    public static int containerDataCount = 1;
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(size) {
        @Override
        protected void onContentsChanged(int slots) {
            setChanged();
        }
    };
    private LazyOptional<IItemHandler> lazyOptional = LazyOptional.empty();
    public static int maxProgress = 78;
    private int progress = 0;
    public static int size = 4;

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
                return containerDataCount;
            }
        };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new AlloyFurnaceContainer(id, inventory, this, this.containerData);
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

            if (alloyFurnaceBlockEntity.progress >= maxProgress) {
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

        boolean hasIngredients = alloyFurnaceBlockEntity.itemStackHandler.getStackInSlot(1).getItem() == Items.COAL;
        boolean canEnterOutput = simpleContainer.getItem(4).isEmpty() || simpleContainer.getItem(4).getItem() == Items.DIAMOND;
        boolean hasEnoughRoomInOutput = simpleContainer.getItem(4).getCount() < simpleContainer.getItem(4).getMaxStackSize();

        return hasIngredients && canEnterOutput && hasEnoughRoomInOutput;
    }

    private static void smeltItem(AlloyFurnaceBlockEntity alloyFurnaceBlockEntity) {
        alloyFurnaceBlockEntity.itemStackHandler.extractItem(1, 1, false);
        alloyFurnaceBlockEntity.itemStackHandler.setStackInSlot(4, new ItemStack(Items.DIAMOND, alloyFurnaceBlockEntity.itemStackHandler.getStackInSlot(4).getCount() + 1));
        alloyFurnaceBlockEntity.progress = 0;
    }
}
