package com.mineboundteam.minebound.blockentity;

import com.mineboundteam.minebound.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SpellHolderBlockEntity extends ChestBlockEntity {
    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return progress;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) progress = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(69);
    private final LazyOptional<IItemHandlerModifiable> lazyOptional = LazyOptional.of(() -> itemStackHandler);
    private int progress = 0;

    public SpellHolderBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.SPELL_HOLDER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @NotNull Direction direction) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyOptional.cast() : super.getCapability(capability, direction);
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @Override
    public void invalidateCaps() {
        lazyOptional.invalidate();
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        itemStackHandler.deserializeNBT(compoundTag.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.put("inventory", this.itemStackHandler.serializeNBT());
    }

    public void tick() {
        if (level == null) return;

        progress++;
        if (progress > 1000) {
            progress = 0;
            Bat bat = new Bat(EntityType.BAT, level);
            bat.setPos(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
            level.addFreshEntity(bat);
        }
    }
}
