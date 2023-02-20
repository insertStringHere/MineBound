package com.mineboundteam.minebound.inventory;

import com.mineboundteam.minebound.inventory.registry.MenuRegistry;
import com.mineboundteam.minebound.registry.RecipeRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class AlloyFurnaceMenu extends RecipeBookMenu<Container> {  
    public AlloyFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData data){
        this(MenuRegistry.ALLOY_FURNACE_MENU.get(), RecipeRegistry.ALLOY_FURNACE_RECIPE.get(), RecipeBookType.FURNACE, containerId, playerInventory, container, data);
    }

    public AlloyFurnaceMenu(MenuType<?> pMenuType, RecipeType<? extends AbstractCookingRecipe> pRecipeType, RecipeBookType pRecipeBookType, int pContainerId, Inventory pPlayerInventory, Container pContainer, ContainerData pData) {
        super(pMenuType, pContainerId);
        this.recipeType = pRecipeType;
        this.recipeBookType = pRecipeBookType;
        checkContainerSize(pContainer, 3);
        checkContainerDataCount(pData, 4);
        this.container = pContainer;
        this.data = pData;
        this.level = pPlayerInventory.player.level;

        addSlots(pPlayerInventory);

        addDataSlots(pData);
    }

    public AlloyFurnaceMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new SimpleContainer(5), new SimpleContainerData(4));
    }

    protected void addSlots(Inventory pPlayerInventory){
        this.addSlot(new Slot(container, 0, 22, 34));
        this.addSlot(new Slot(container, 3, 45, 21));
        this.addSlot(new Slot(container, 4, 68, 34));
        this.addSlot(new FuckingFuelSlot(this, container, 1, 45, 62));
        this.addSlot(new FurnaceResultSlot(pPlayerInventory.player, container, 2, 139, 34));

        addInventory(pPlayerInventory);
        addHotBar(pPlayerInventory);
    }

    protected void addHotBar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    protected void addInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex == 4) {
                if (!this.moveItemStackTo(itemstack1, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (pIndex > 4) {
                if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.canSmelt(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < 32) {
                    if (!this.moveItemStackTo(itemstack1, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < 41 && !this.moveItemStackTo(itemstack1, 5, 32, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 5, 32, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }


    // Copied from AbstractFurnaceMenu because slots are set up in the constructor
    // and java is a BAD language and minecraft is a POORLY written game. Fuck Mojang and fuck Oracle
    protected final Container container;
    protected final ContainerData data;
    protected final Level level;
    protected final RecipeType<? extends AbstractCookingRecipe> recipeType;
    protected final RecipeBookType recipeBookType;
 
    public void fillCraftSlotsStackedContents(StackedContents pItemHelper) {
       if (this.container instanceof StackedContentsCompatible) {
          ((StackedContentsCompatible)this.container).fillStackedContents(pItemHelper);
       }
 
    }
 
    public void clearCraftingContent() {
       this.getSlot(0).set(ItemStack.EMPTY);
       this.getSlot(2).set(ItemStack.EMPTY);
    }
 
    public boolean recipeMatches(Recipe<? super Container> pRecipe) {
       return pRecipe.matches(this.container, this.level);
    }
 
    public int getResultSlotIndex() {
       return 2;
    }
 
    public int getGridWidth() {
       return 1;
    }
 
    public int getGridHeight() {
       return 1;
    }
 
    public int getSize() {
       return 3;
    }
 
    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(Player pPlayer) {
       return this.container.stillValid(pPlayer);
    }
    
    protected boolean canSmelt(ItemStack pStack) {
       return this.level.getRecipeManager().getRecipes().stream().anyMatch((r) -> r.getIngredients().stream().anyMatch((i) -> i.test(pStack)));
    }
 
    protected boolean isFuel(ItemStack pStack) {
       return net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, this.recipeType) > 0;
    }
 
    public int getBurnProgress() {
       int i = this.data.get(AbstractFurnaceBlockEntity.DATA_COOKING_PROGRESS);
       int j = this.data.get(AbstractFurnaceBlockEntity.DATA_COOKING_TOTAL_TIME);
       return j != 0 && i != 0 ? i * 24 / j : 0;
    }
 
    public int getLitProgress() {
       int i = this.data.get(1);
       if (i == 0) {
          i = 200;
       }
 
       return this.data.get(0) * 13 / i;
    }
 
    public boolean isLit() {
       return this.data.get(0) > 0;
    }
 
    public RecipeBookType getRecipeBookType() {
       return this.recipeBookType;
    }
 
    public boolean shouldMoveToInventory(int pSlotIndex) {
       return pSlotIndex != 1;
    }

    // This game makes me want to strangle someone
    protected class FuckingFuelSlot extends FurnaceFuelSlot{
        AlloyFurnaceMenu menu; 
        public FuckingFuelSlot(AlloyFurnaceMenu pFurnaceMenu, Container pFurnaceContainer, int pSlot, int pXPosition, int pYPosition) {
            super(null, pFurnaceContainer, pSlot, pXPosition, pYPosition);
            this.menu = pFurnaceMenu;
         }

         public boolean mayPlace(ItemStack pStack) {
            return this.menu.isFuel(pStack) || isBucket(pStack);
         }
      
         public int getMaxStackSize(ItemStack pStack) {
            return isBucket(pStack) ? 1 : super.getMaxStackSize(pStack);
         }
      
         public static boolean isBucket(ItemStack pStack) {
            return pStack.is(Items.BUCKET);
         }
    }
}
