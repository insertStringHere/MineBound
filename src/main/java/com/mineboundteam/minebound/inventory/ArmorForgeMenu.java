package com.mineboundteam.minebound.inventory;

import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.crafting.ArmorForgeRecipe;
import com.mineboundteam.minebound.inventory.containers.ArmorSpellContainer;
import com.mineboundteam.minebound.inventory.slots.InputArmorSlot;
import com.mineboundteam.minebound.inventory.slots.MyrialSpellSlot;
import com.mineboundteam.minebound.inventory.slots.OutputArmorSlot;
import com.mineboundteam.minebound.inventory.slots.PlayerArmorSlot;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.registry.MenuRegistry;
import com.mineboundteam.minebound.registry.RecipeRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
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

    private final ResultContainer resultContainer = new ResultContainer() {
        @Override
        public ItemStack removeItem(int index, int count) {
            if (slots.get(ARMOR_INPUT_INDEX).hasItem())
                transferStoredItems(getItem(index), slots.get(ARMOR_INPUT_INDEX).getItem());
            return super.removeItem(index, count);
        }

        // Fix it when you implement the recipe if it doesn't work
        // I'm hecking tired
        protected void transferStoredItems(ItemStack result, ItemStack source) {
            if (!player.level.isClientSide()) {
                result.getCapability(ArmorSpellsProvider.ARMOR_ACTIVE_SPELLS).ifPresent(slots -> {
                    int max = ((MyrialArmorItem) result.getItem()).getConfig().STORAGE_SLOTS.get();
                    source.getCapability(ArmorSpellsProvider.ARMOR_ACTIVE_SPELLS).ifPresent(mySlots -> {
                        for (int i = 0; i < mySlots.items.size(); i++) {
                            if (i < max) {
                                slots.items.add(mySlots.items.get(i));
                            } else if (!moveItemStackTo(mySlots.items.get(i), 0, 40, false)) {
                                player.drop(mySlots.items.get(i), false, false);
                            }
                        }
                    });
                });
                result.getCapability(ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS).ifPresent(slots -> {
                    int max = ((MyrialArmorItem) result.getItem()).getConfig().UTILITY_SLOTS.get();
                    source.getCapability(ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS).ifPresent(mySlots -> {
                        for (int i = 0; i < mySlots.items.size(); i++) {
                            if (i < max) {
                                slots.items.add(mySlots.items.get(i));
                            } else if (!moveItemStackTo(mySlots.items.get(i), 0, 40, false)) {
                                player.drop(mySlots.items.get(i), false, false);
                            }
                        }
                    });
                });
            }
        }
    };

    public ArmorSpellContainer activeSpells;
    public ArmorSpellContainer passiveSpells;

    public final Player player;
    public static final int SIZE = 7;
    public static final int ARMOR_INPUT_INDEX = 45;

    public ArmorForgeMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public ArmorForgeMenu(int containerID, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(MenuRegistry.ARMOR_FORGE_MENU.get(), containerID);
        this.containerLevelAccess = containerLevelAccess;
        this.player = inventory.player;
        addSlots(inventory);
    }

    protected void addSlots(Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 32 + i * 18, 182));
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 32 + j * 18, 124 + i * 18));
            }
        }

        this.addSlot(new PlayerArmorSlot(EquipmentSlot.FEET, player, inventory, 36, 8, 76));
        this.addSlot(new PlayerArmorSlot(EquipmentSlot.LEGS, player, inventory, 37, 8, 58));
        this.addSlot(new PlayerArmorSlot(EquipmentSlot.CHEST, player, inventory, 38, 8, 40));
        this.addSlot(new PlayerArmorSlot(EquipmentSlot.HEAD, player, inventory, 39, 8, 22));

        this.addSlot(new Slot(this.craftingContainer, 0, 75, 19));
        this.addSlot(new Slot(this.craftingContainer, 1, 107, 42));
        this.addSlot(new Slot(this.craftingContainer, 2, 93, 81));
        this.addSlot(new Slot(this.craftingContainer, 3, 57, 81));
        this.addSlot(new Slot(this.craftingContainer, 4, 43, 42));

        this.addSlot(new InputArmorSlot(inventory.player, this.craftingContainer, 5, 75, 49));
        this.addSlot(new OutputArmorSlot(inventory.player, this.craftingContainer, this.resultContainer, 0, 168, 52));

        activeSpells = new ArmorSpellContainer.ActiveSpell(this);
        passiveSpells = new ArmorSpellContainer.PassiveSpell(this);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                this.addSlot(new MyrialSpellSlot.Active(activeSpells, i * 3 + j, 208 + 21 * j, 26 + 21 * i));
            }
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                this.addSlot(new MyrialSpellSlot.Passive(passiveSpells, i * 3 + j, 208 + 21 * j, 103 + 21 * i));
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
        return SIZE;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem())
            return ItemStack.EMPTY;
        ItemStack sourceStack = slot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        // if in inventory, first try to move to armor slots, then into crafting slots
        if (index < 36) {
            if (!moveItemStackTo(sourceStack, 36, 40, false)
                    && !moveItemStackTo(sourceStack, 40, 47, false)
                    && !moveItemStackTo(sourceStack, 0, 36, false))
                return ItemStack.EMPTY;
            // If in armor slots, try to move to armor crafting slot, then if not, into inventory
        } else if (index < 40) {
            if (!moveItemStackTo(sourceStack, 45, 45, false)
                    && !moveItemStackTo(sourceStack, 0, 36, false))
                return ItemStack.EMPTY;
            // If result slot, first try to move to armor inventory, then if not, into inventory
        } else if (index > 45) {
            if (!moveItemStackTo(sourceStack, 36, 40, false)
                    && !moveItemStackTo(sourceStack, 45, 45, false)
                    && !moveItemStackTo(sourceStack, 0, 40, false))
                return ItemStack.EMPTY;
            // Try to equip the armor first
        } else if (index == 45) {
            if (!moveItemStackTo(sourceStack, 36, 40, false)
                    && !moveItemStackTo(sourceStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else if (!moveItemStackTo(sourceStack, 0, 40, false))
            return ItemStack.EMPTY;

        if (sourceStack.getCount() == 0) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        slot.onTake(player, sourceStack);
        return copyOfSourceStack;
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

    protected static void slotChangedCraftingGrid(ArmorForgeMenu armorForgeMenu, Level level, Player player,
            CraftingContainer craftingContainer, ResultContainer resultContainer) {
        if (level.isClientSide)
            return;

        ServerPlayer serverplayer = (ServerPlayer) player;
        if (level.getServer() == null)
            return;

        ItemStack itemStack = ItemStack.EMPTY;
        Optional<ArmorForgeRecipe> optional = level.getServer().getRecipeManager()
                .getRecipeFor(RecipeRegistry.ARMOR_FORGE_RECIPE.get(), craftingContainer, level);
        if (optional.isPresent()) {
            ArmorForgeRecipe armorForgeRecipe = optional.get();
            if (resultContainer.setRecipeUsed(level, serverplayer, armorForgeRecipe)) {
                itemStack = armorForgeRecipe.getResultItem();

            }
        }

        resultContainer.setItem(0, itemStack);
        armorForgeMenu.setRemoteSlot(0, itemStack);
        serverplayer.connection.send(new ClientboundContainerSetSlotPacket(armorForgeMenu.containerId,
                armorForgeMenu.incrementStateId(), 0, itemStack));

    }

    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.containerLevelAccess.execute((a, b) -> {
            this.clearContainer(pPlayer, craftingContainer);
        });
    }
}
