package com.mineboundteam.minebound.block.entity;

import com.mineboundteam.minebound.container.AlloyFurnaceMenu;
import com.mineboundteam.minebound.registry.BlockRegistry;
import com.mineboundteam.minebound.registry.RecipeRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AlloyFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    public AlloyFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockRegistry.ALLOY_FURNACE_ENTITY.get(), pPos, pBlockState, RecipeRegistry.ALLOY_FURNACE_RECIPE.get());
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent( "container.alloy_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new AlloyFurnaceMenu(pContainerId, pInventory, this, this.dataAccess);
    }

    public ContainerData getContainerData(){
        return dataAccess; 
    }

}
