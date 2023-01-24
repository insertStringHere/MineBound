package com.mineboundteam.minebound.container;

import com.mineboundteam.minebound.block.entity.AlloyFurnaceBlockEntity;
import com.mineboundteam.minebound.registry.ContainerRegistry;
import com.mineboundteam.minebound.registry.RecipeRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class AlloyFurnaceMenu extends AbstractFurnaceMenu {
    protected AlloyFurnaceMenu(int pContainerId, Inventory pPlayerInventory) {
        super(ContainerRegistry.ALLOY_FURNACE_CONTAINER.get(), RecipeRegistry.ALLOY_FURNACE_RECIPE.get(),
                RecipeBookType.FURNACE, pContainerId, pPlayerInventory);
    }

    public AlloyFurnaceMenu(int containerId, Inventory playerInventory, Container container,
            ContainerData containerData) {
        this(containerId, playerInventory);
        this.slots.clear();

        this.addSlot(new Slot(container, 0, 21, 48));
        this.addSlot(new FurnaceFuelSlot(this, container, 1, 45, 48));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, container, 2, 69, 48));
        this.addSlot(new Slot(container, 3, 104, 22));
        this.addSlot(new Slot(container, 4, 139, 48));

        addInventory(playerInventory);
        addHotBar(playerInventory);
        addDataSlots(containerData);
    }

    public AlloyFurnaceMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new SimpleContainer(5),
                ((AlloyFurnaceBlockEntity) playerInventory.player.level.getBlockEntity(buf.readBlockPos()))
                        .getContainerData());
    }

    private void addHotBar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    private void addInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack sort = super.quickMoveStack(player, index);

        return sort;
    }

    @Override
    protected boolean canSmelt(ItemStack pStack) {
        return super.canSmelt(pStack); 
    }

    @Override
    public boolean recipeMatches(Recipe<? super Container> pRecipe) {
        // TODO Auto-generated method stub
        return false;
    }
}
