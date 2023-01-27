package com.mineboundteam.minebound.inventory;

import com.mineboundteam.minebound.crafting.ArmorForgeRecipe;
import com.mineboundteam.minebound.registry.MenuRegistry;
import com.mineboundteam.minebound.registry.RecipeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ArmorForgeMenu extends RecipeBookMenu<CraftingContainer> {
    private final ContainerLevelAccess containerLevelAccess;
    private final CraftingContainer craftingContainer = new CraftingContainer(this, 3, 3);
    private final Player player;
    private final ResultContainer resultContainer = new ResultContainer();

    public ArmorForgeMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public ArmorForgeMenu(int containerID, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(MenuRegistry.ARMOR_FORGE_MENU.get(), containerID);
        this.containerLevelAccess = containerLevelAccess;
        this.player = inventory.player;

        this.addSlot(new ResultSlot(inventory.player, this.craftingContainer, this.resultContainer, 0, 124, 35));
        this.addSlot(new Slot(this.craftingContainer, 0, 10, 10));
        this.addSlot(new Slot(this.craftingContainer, 1, 30, 10));
        this.addSlot(new Slot(this.craftingContainer, 2, 50, 10));
        this.addSlot(new Slot(this.craftingContainer, 3, 70, 10));
        this.addSlot(new Slot(this.craftingContainer, 4, 90, 10));
        this.addSlot(new Slot(this.craftingContainer, 5, 10, 30));
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    public void clearCraftingContent() {
        this.craftingContainer.clearContent();
        this.resultContainer.clearContent();
    }

    public void fillCraftSlotsStackedContents(@NotNull StackedContents stackedContents) {
        this.craftingContainer.fillStackedContents(stackedContents);
    }

    public int getGridHeight() {
        return this.craftingContainer.getHeight();
    }

    public int getGridWidth() {
        return this.craftingContainer.getWidth();
    }

    public @NotNull RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    public int getResultSlotIndex() {
        return 0;
    }

    public int getSize() {
        return 7;
    }

    public boolean recipeMatches(Recipe<? super CraftingContainer> recipe) {
        return recipe.matches(this.craftingContainer, this.player.level);
    }

    public boolean shouldMoveToInventory(int slotIndex) {
        return slotIndex != this.getResultSlotIndex();
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        this.containerLevelAccess.execute((level, blockPos) -> {
            slotChangedCraftingGrid(this, level, this.player, this.craftingContainer, this.resultContainer);
        });
    }

    protected static void slotChangedCraftingGrid(ArmorForgeMenu armorForgeMenu, Level level, Player player, CraftingContainer craftingContainer, ResultContainer resultContainer) {
        if (level.isClientSide) return;

        ServerPlayer serverplayer = (ServerPlayer) player;
        if (level.getServer() == null) return;

        ItemStack itemStack = ItemStack.EMPTY;
        Optional<ArmorForgeRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeRegistry.ARMOR_FORGE_RECIPE.get(), craftingContainer, level);
        System.out.println(optional);
        if (optional.isPresent()) {
            ArmorForgeRecipe armorForgeRecipe = optional.get();
            if (resultContainer.setRecipeUsed(level, serverplayer, armorForgeRecipe)) {
                itemStack = armorForgeRecipe.assemble(craftingContainer);
            }
        }

        resultContainer.setItem(0, itemStack);
        armorForgeMenu.setRemoteSlot(0, itemStack);
        serverplayer.connection.send(new ClientboundContainerSetSlotPacket(armorForgeMenu.containerId, armorForgeMenu.incrementStateId(), 0, itemStack));
    }

    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
