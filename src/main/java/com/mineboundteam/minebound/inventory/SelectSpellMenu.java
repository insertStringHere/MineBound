package com.mineboundteam.minebound.inventory;

import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;
import com.mineboundteam.minebound.inventory.containers.InventorySpellContainer;
import com.mineboundteam.minebound.inventory.registry.MenuRegistry;
import com.mineboundteam.minebound.inventory.slots.SelectedSpellSlot;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

public class SelectSpellMenu extends AbstractContainerMenu {

    public final Capability<SelectedSpell> cap;

    public final InventorySpellContainer activeSpells;
    public final InventorySpellContainer passiveSpells;

    public SelectSpellMenu(int pContainerId, Inventory playerInventory, boolean isPrimary) {
        super(MenuRegistry.SELECT_SPELL_MENU.get(), pContainerId);
        this.cap = isPrimary ? PlayerSelectedSpellsProvider.PRIMARY_SPELL : PlayerSelectedSpellsProvider.SECONDARY_SPELL;

        activeSpells = new InventorySpellContainer(playerInventory, this.cap, ArmorSpellsProvider.ARMOR_ACTIVE_SPELLS, isPrimary);
        passiveSpells = new InventorySpellContainer(playerInventory, this.cap, ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS, isPrimary);

        addSlots();
    }

    protected void addSlots(){
        for(int i = 0; i < passiveSpells.getContainerSize() / 2; i++)
            for(int j = 0; j < 2 && i*2 + j < passiveSpells.getContainerSize(); j++)
                this.addSlot(new SelectedSpellSlot(passiveSpells, i*2+j, 5+j*20, 10+18*i));

        for(int i = 0; i < activeSpells.getContainerSize() / 6; i++)
            for(int j = 0; j < 6 && i*6+j < activeSpells.getContainerSize(); j++)
                this.addSlot(new SelectedSpellSlot(activeSpells, i*6+j, 117+i*19, 36+j*19));
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    public SelectSpellMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, buf.readBoolean());
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
    
}
