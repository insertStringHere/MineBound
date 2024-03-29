package com.mineboundteam.minebound.inventory;

import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.crafting.ArmorForgeRecipe;
import com.mineboundteam.minebound.inventory.containers.ArmorSpellContainer;
import com.mineboundteam.minebound.inventory.registry.MenuRegistry;
import com.mineboundteam.minebound.inventory.registry.RecipeRegistry;
import com.mineboundteam.minebound.inventory.slots.InputArmorSlot;
import com.mineboundteam.minebound.inventory.slots.MyrialSpellSlot;
import com.mineboundteam.minebound.inventory.slots.OutputArmorSlot;
import com.mineboundteam.minebound.inventory.slots.PlayerArmorSlot;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.magic.SpellItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

import java.util.List;
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

        protected void transferStoredItems(ItemStack result, ItemStack source) {
            List<ItemStack> activeSrc = ArmorNBTHelper.getSpellTag(source, ArmorNBTHelper.ACTIVE_SPELL).stream().map(t -> {
                if(t instanceof CompoundTag tag)
                    return ItemStack.of(tag);
                return ItemStack.EMPTY;
            }).filter(i -> !i.isEmpty()).toList();
            ListTag activeDst = new ListTag(); 

            List<ItemStack> passiveSrc = ArmorNBTHelper.getSpellTag(source, ArmorNBTHelper.PASSIVE_SPELL).stream().map(t -> {
                if(t instanceof CompoundTag tag)
                    return ItemStack.of(tag);
                return ItemStack.EMPTY;
            }).filter(i -> !i.isEmpty()).toList();
            ListTag passiveDst = new ListTag();

            int max = activeSpells.armorCount;
            for (ItemStack spell : activeSrc) {
                if (max <= 0 && !moveItemStackTo(spell, 0, 40, false)) {
                    player.drop(spell, false, false);
                } else {
                    activeDst.add(spell.serializeNBT());
                    max--;
                }
            }

            max = passiveSpells.armorCount;
            for (ItemStack spell : passiveSrc) {
                if (max <= 0 && !moveItemStackTo(spell, 0, 40, false)) {
                    player.drop(spell, false, false);
                } else {
                    passiveDst.add(spell.serializeNBT());
                    max--;
                }
            }

            ArmorNBTHelper.saveSpellTag(result, ArmorNBTHelper.ACTIVE_SPELL, activeDst);
            ArmorNBTHelper.saveSpellTag(result, ArmorNBTHelper.PASSIVE_SPELL, passiveDst);
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

        this.addSlot(new InputArmorSlot(this, this.craftingContainer, 5, 75, 49));
        this.addSlot(new OutputArmorSlot(inventory.player, this.craftingContainer, this.resultContainer, 0, 168, 52));

        activeSpells = new ArmorSpellContainer(this, ArmorNBTHelper.ACTIVE_SPELL);
        passiveSpells = new ArmorSpellContainer(this, ArmorNBTHelper.PASSIVE_SPELL);

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
            if (sourceStack.getItem() instanceof SpellItem && slots.get(ARMOR_INPUT_INDEX).hasItem()) {
                //If Spell Item and there is an Armor Item, Attempt to Move to Spell Slots
                ItemStack armorSlotItem = slots.get(ARMOR_INPUT_INDEX).getItem();
                if (armorSlotItem.getItem() instanceof MyrialArmorItem armorItem) {
                    Integer utilitySlots = armorItem.getConfig().UTILITY_SLOTS.get();
                    Integer activeSlots = armorItem.getConfig().STORAGE_SLOTS.get();
                    if (!moveItemStackTo(sourceStack, 47, 47 + activeSlots, false) //Attempt to Move to Active Spell Slots
                            && !moveItemStackTo(sourceStack, 56, 56 + utilitySlots, false) //Attempt to Move Utility Slots
                            && !moveItemStackTo(sourceStack, 0, 36, false)) //Attempt to Move to Player Inventory
                        return ItemStack.EMPTY;
                }
            }
            if (!moveItemStackTo(sourceStack, ARMOR_INPUT_INDEX, ARMOR_INPUT_INDEX+1, false) //Attempt to Move Armor Input Slot
                    && !moveItemStackTo(sourceStack, 36, 40, false) //Attempt to Move to Armor Slots
                    && !moveItemStackTo(sourceStack, 40, 47, false) //Attempt to Move to Armor Crafting Slots
                    && !moveItemStackTo(sourceStack, 0, 36, false)) //Attempt to Move to Player Inventory
                return ItemStack.EMPTY;
            // If in armor slots, try to move to armor crafting slot, then if not, into inventory
        } else if (index < 40) {
            if (!moveItemStackTo(sourceStack, ARMOR_INPUT_INDEX, ARMOR_INPUT_INDEX+1, false)
                    && !moveItemStackTo(sourceStack, 0, 36, false))
                return ItemStack.EMPTY;
            // If result slot, first try to move to armor inventory, then if not, into inventory
        } else if (index > ARMOR_INPUT_INDEX) {
            if (!moveItemStackTo(sourceStack, 36, 40, false)
                    && !moveItemStackTo(sourceStack, ARMOR_INPUT_INDEX, ARMOR_INPUT_INDEX+1, false)
                    && !moveItemStackTo(sourceStack, 0, 40, false))
                return ItemStack.EMPTY;
            // Try to equip the armor first
        } else if (index == ARMOR_INPUT_INDEX) {
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
