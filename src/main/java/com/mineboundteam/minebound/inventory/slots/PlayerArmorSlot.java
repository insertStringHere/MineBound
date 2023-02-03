package com.mineboundteam.minebound.inventory.slots;

import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class PlayerArmorSlot extends Slot {
    EquipmentSlot mySlot;
    Player player;
    public static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[] {
            InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET };

    public PlayerArmorSlot(EquipmentSlot mySlot, Player myPlayer, Container container, int slotIndex, int x,
            int y) {
        super(container, slotIndex, x, y);
        this.mySlot = mySlot;
        this.player = myPlayer;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack input) {
        return input.canEquip(mySlot, player);
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack itemstack = this.getItem();
        return !itemstack.isEmpty() && EnchantmentHelper.hasBindingCurse(itemstack) ? false
                : super.mayPickup(player);
    }

    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[mySlot.getIndex()]);
    }
}