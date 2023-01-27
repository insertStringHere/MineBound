package com.mineboundteam.minebound.inventory;

import com.mineboundteam.minebound.block.entity.ArmorForgeBlockEntity;
import com.mineboundteam.minebound.registry.BlockRegistry;
import com.mineboundteam.minebound.registry.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ArmorForgeMenu extends AbstractContainerMenu {
    public final ArmorForgeBlockEntity armorForgeBlockEntity;
    private final Level level;

    public ArmorForgeMenu(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(id, inventory, inventory.player.level.getBlockEntity(friendlyByteBuf.readBlockPos()));
    }

    public ArmorForgeMenu(int id, Inventory inventory, BlockEntity blockEntity) {
        super(MenuRegistry.ARMOR_FORGE_CONTAINER.get(), id);
        checkContainerSize(inventory, 3);
        armorForgeBlockEntity = (ArmorForgeBlockEntity) blockEntity;
        this.level = inventory.player.level;

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 144));
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
        this.armorForgeBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 10, 10));
            this.addSlot(new SlotItemHandler(handler, 1, 30, 10));
            this.addSlot(new SlotItemHandler(handler, 2, 50, 10));
            this.addSlot(new SlotItemHandler(handler, 3, 70, 10));
            this.addSlot(new SlotItemHandler(handler, 4, 90, 10));
            this.addSlot(new SlotItemHandler(handler, 5, 10, 30));
        });
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, armorForgeBlockEntity.getBlockPos()), player, BlockRegistry.ARMOR_FORGE.get());
    }
}
