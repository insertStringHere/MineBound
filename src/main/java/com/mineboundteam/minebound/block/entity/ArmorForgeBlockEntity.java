package com.mineboundteam.minebound.block.entity;

import com.mineboundteam.minebound.crafting.AlloyFurnaceRecipe;
import com.mineboundteam.minebound.inventory.ArmorForgeMenu;
import com.mineboundteam.minebound.registry.BlockRegistry;
import com.mineboundteam.minebound.registry.RecipeRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ArmorForgeBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    public static final int SIZE = 6;

    public ArmorForgeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockRegistry.ARMOR_FORGE_ENTITY.get(), blockPos, blockState);
    }

    private static boolean canCraft(SimpleContainer simpleContainer, Optional<AlloyFurnaceRecipe> alloyFurnaceRecipe) {
        boolean hasRecipe = alloyFurnaceRecipe.isPresent() && (simpleContainer.getItem(SIZE - 1).getItem() == alloyFurnaceRecipe.get().getResultItem().getItem() || simpleContainer.getItem(SIZE - 1).isEmpty());
        boolean hasEnoughRoom = simpleContainer.getItem(SIZE - 1).getCount() < simpleContainer.getItem(SIZE - 1).getMaxStackSize();
        return hasRecipe && hasEnoughRoom;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, @NotNull Inventory inventory, @NotNull Player player) {
        return new ArmorForgeMenu(containerID, inventory, this);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return new TranslatableComponent("container.alloy_furnace");
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(capability, direction);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, ArmorForgeBlockEntity armorForgeBlockEntity) {
        if (level.isClientSide()) {
            return;
        }

        SimpleContainer simpleContainer = new SimpleContainer(armorForgeBlockEntity.itemStackHandler.getSlots());
        for (int i = 0; i < armorForgeBlockEntity.itemStackHandler.getSlots(); i++) {
            simpleContainer.setItem(i, armorForgeBlockEntity.itemStackHandler.getStackInSlot(i));
        }
        Optional<AlloyFurnaceRecipe> alloyFurnaceRecipe = level.getRecipeManager().getRecipeFor(RecipeRegistry.ALLOY_FURNACE_RECIPE.get(), simpleContainer, level);

        if (canCraft(simpleContainer, alloyFurnaceRecipe)) {
            armorForgeBlockEntity.itemStackHandler.setStackInSlot(SIZE - 1, new ItemStack(alloyFurnaceRecipe.get().getResultItem().getItem(),armorForgeBlockEntity.itemStackHandler.getStackInSlot(SIZE - 1).getCount() + 1));
        }
    }
}
