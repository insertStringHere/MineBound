package com.mineboundteam.minebound.block.entity;

import com.mineboundteam.minebound.inventory.AlloyFurnaceMenu;
import com.mineboundteam.minebound.registry.BlockRegistry;
import com.mineboundteam.minebound.registry.RecipeRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@SuppressWarnings("unchecked") // suck a chode java.
public class AlloyFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    protected static RecipeType<? extends AbstractCookingRecipe> recipeType = RecipeRegistry.ALLOY_FURNACE_RECIPE.get();

    public AlloyFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockRegistry.ALLOY_FURNACE_ENTITY.get(), pPos, pBlockState, recipeType);
        items = NonNullList.withSize(5, ItemStack.EMPTY);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.alloy_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new AlloyFurnaceMenu(pContainerId, pInventory, this, this.dataAccess);
    }

    public ContainerData getContainerData() {
        return dataAccess;
    }


    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, AlloyFurnaceBlockEntity pBlockEntity) {
        boolean flag = pBlockEntity.isLit();
        boolean flag1 = false;
        if (pBlockEntity.isLit()) {
            pBlockEntity.dataAccess.set(DATA_LIT_TIME, pBlockEntity.dataAccess.get(DATA_LIT_TIME)-1);
        }
  
        ItemStack itemstack = pBlockEntity.items.get(1);
        if (pBlockEntity.isLit() || !itemstack.isEmpty() && !slotsEmpty(pBlockEntity.items)) {
           Recipe<?> recipe = pLevel.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>)AlloyFurnaceBlockEntity.recipeType, pBlockEntity, pLevel).orElse(null);
           int i = pBlockEntity.getMaxStackSize();

           // Fuel management
           if (!pBlockEntity.isLit() && pBlockEntity.canBurn(recipe, pBlockEntity.items, i)) {
              pBlockEntity.dataAccess.set(DATA_LIT_TIME, pBlockEntity.getBurnDuration(itemstack));
              pBlockEntity.dataAccess.set(DATA_LIT_DURATION, pBlockEntity.dataAccess.get(DATA_LIT_TIME));
              if (pBlockEntity.isLit()) {
                 flag1 = true;
                 if (itemstack.hasContainerItem())
                    pBlockEntity.items.set(1, itemstack.getContainerItem());
                 else
                 if (!itemstack.isEmpty()) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                       pBlockEntity.items.set(1, itemstack.getContainerItem());
                    }
                 }
              }
           }
  
           if (pBlockEntity.isLit() && pBlockEntity.canBurn(recipe, pBlockEntity.items, i)) {
              pBlockEntity.dataAccess.set(DATA_COOKING_PROGRESS, pBlockEntity.dataAccess.get(DATA_COOKING_PROGRESS) + 1);
              if (pBlockEntity.dataAccess.get(DATA_COOKING_PROGRESS) == pBlockEntity.dataAccess.get(DATA_COOKING_TOTAL_TIME)) {
                 pBlockEntity.dataAccess.set(DATA_COOKING_PROGRESS, 0);
                 pBlockEntity.dataAccess.set(DATA_COOKING_TOTAL_TIME, getTotalCookTime(pLevel, AlloyFurnaceBlockEntity.recipeType, pBlockEntity));
                 if (pBlockEntity.burn(recipe, pBlockEntity.items, i)) {
                    pBlockEntity.setRecipeUsed(recipe);
                 }
  
                 flag1 = true;
              }
           } else {
            pBlockEntity.dataAccess.set(DATA_COOKING_PROGRESS, 0);
           }
        } else if (!pBlockEntity.isLit() && pBlockEntity.dataAccess.get(DATA_COOKING_PROGRESS) > 0) {
            pBlockEntity.dataAccess.set(DATA_COOKING_PROGRESS, Mth.clamp(pBlockEntity.dataAccess.get(DATA_COOKING_PROGRESS) - 2, 0, pBlockEntity.dataAccess.get(DATA_COOKING_TOTAL_TIME)));
        }
  
        if (flag != pBlockEntity.isLit()) {
           flag1 = true;
           pState = pState.setValue(BlockStateProperties.LIT, Boolean.valueOf(pBlockEntity.isLit()));
           pLevel.setBlock(pPos, pState, 3);
        }
  
        if (flag1) {
           setChanged(pLevel, pPos, pState);
        }
  
    }

    // This game was written by toddlers with crayons; why the fuck aren't these methods protected??????

    protected static boolean slotsEmpty(NonNullList<ItemStack> items) {
        return (items.get(0).isEmpty() && items.get(3).isEmpty() && items.get(4).isEmpty());
    }

    protected boolean isLit() {
        return dataAccess.get(DATA_LIT_TIME) > 0;
    }

    protected boolean canBurn(Recipe<?> pRecipe, NonNullList<ItemStack> pStacks, int pStackSize) {
        if (!slotsEmpty(pStacks) && pRecipe != null) {
            ItemStack itemstack = ((Recipe<WorldlyContainer>) pRecipe).assemble(this);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = pStacks.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= pStackSize
                        && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    protected boolean burn(Recipe<?> pRecipe, NonNullList<ItemStack> pStacks, int pStackSize) {
        if (pRecipe != null && this.canBurn(pRecipe, pStacks, pStackSize)) {
            ItemStack itemstack = pStacks.get(0);
            ItemStack itemstack1 = ((Recipe<WorldlyContainer>) pRecipe).assemble(this);
            ItemStack itemstack2 = pStacks.get(2);
            ItemStack itemStack3 = pStacks.get(3);
            ItemStack itemStack4 = pStacks.get(4);
            if (itemstack2.isEmpty()) {
                pStacks.set(2, itemstack1.copy());
            } else if (itemstack2.is(itemstack1.getItem())) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (!itemstack.isEmpty())
                itemstack.shrink(1);
            if (!itemStack3.isEmpty())
                itemStack3.shrink(1);
            if (!itemStack4.isEmpty())
                itemStack4.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    protected static int getTotalCookTime(Level pLevel, RecipeType<? extends AbstractCookingRecipe> pRecipeType, Container pContainer) {
        return pLevel.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>)pRecipeType, pContainer, pLevel).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }
}
