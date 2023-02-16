package com.mineboundteam.minebound.inventory.containers;

import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider.SpellContainer;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;
import com.mineboundteam.minebound.capabilities.network.CapabilitySync;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.PacketDistributor;

public class InventorySpellContainer implements Container {
    public final Capability<SelectedSpell> cap;
    public final Capability<SpellContainer> source; 

    protected final Inventory inventory;
    protected final boolean hand; 
    protected final NonNullList<ItemStack> spells = NonNullList.create();

    public InventorySpellContainer(Inventory pInventory, Capability<SelectedSpell> pCapability, Capability<SpellContainer> pSource, boolean isPrimary){
        this.inventory = pInventory;
        this.cap = pCapability; 
        this.source = pSource;
        this.hand = isPrimary; 

        refreshSpells();
    }

    protected void refreshSpells(){
        spells.clear();
        for(ItemStack armor : inventory.armor)
            armor.getCapability(source).ifPresent(slots -> {
                for(ItemStack slot : slots.items) 
                    spells.add(slot);
            });
    }

    @Override
    public void clearContent() {
        // Not an option;
    }

    @Override
    public int getContainerSize() {
        return spells.size();
    }

    @Override
    public boolean isEmpty() {
        return spells.isEmpty();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return spells.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        if(!inventory.player.level.isClientSide()) {
        var values = new Object() { 
            EquipmentSlot slot = Player.getEquipmentSlotForItem(spells.get(pSlot));
            int index = 0;
            boolean newObj = false;
        };
        
        inventory.player.getItemBySlot(values.slot).getCapability(source).ifPresent(slots -> {
            values.index = slots.items.indexOf(spells.get(pSlot));
        });
        
        inventory.player.getCapability(hand ? PlayerSelectedSpellsProvider.SECONDARY_SPELL :  PlayerSelectedSpellsProvider.PRIMARY_SPELL)
            .ifPresent(selected -> {
                values.newObj = !(selected.index == values.index && selected.equippedSlot.equals(values.slot)); 
            });

        if(values.newObj)
            inventory.player.getCapability(cap).ifPresent(selected -> {
                selected.equippedSlot = values.slot;
                selected.index = values.index;
                
                CapabilitySync.NET_CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer)inventory.player), 
                    new CapabilitySync.SelectedSpellsSync(hand, selected.equippedSlot, selected.index));
            });
        }
        inventory.player.closeContainer();
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        // not an option
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        // Not an option
    }

    @Override
    public void setChanged() {
        refreshSpells();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
    
}
