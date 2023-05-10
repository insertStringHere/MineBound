package com.mineboundteam.minebound.inventory.containers;

import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;
import com.mineboundteam.minebound.capabilities.network.CapabilitySync;
import com.mineboundteam.minebound.capabilities.network.SelectedSpellsSync;
import com.mineboundteam.minebound.magic.events.MagicSelectedEvent;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class InventorySpellContainer implements Container {
    public final Capability<? extends SelectedSpell> cap;
    public final String source;

    protected final Inventory inventory;
    protected final boolean isPrimary;
    protected final NonNullList<Tuple<EquipmentSlot, ItemStack>> spells = NonNullList.create();

    public int primarySelected;
    public int secondarySelected;

    public InventorySpellContainer(Inventory pInventory, Capability<? extends SelectedSpell> pCapability,
            String pSource, boolean isPrimary) {
        this.inventory = pInventory;
        this.cap = pCapability;
        this.source = pSource;
        this.isPrimary = isPrimary;

        refreshSpells();
    }

    protected void refreshSpells() {
        spells.clear();

        var primarySpell = inventory.player.getCapability(PlayerSelectedSpellsProvider.PRIMARY_SPELL);
        var secondarySpell = inventory.player.getCapability(PlayerSelectedSpellsProvider.SECONDARY_SPELL);

        if (!primarySpell.isPresent())
            primarySelected = -1;
        if (!secondarySpell.isPresent())
            secondarySelected = -1;

        primarySpell.ifPresent(pSelect -> {
            secondarySpell.ifPresent(sSelect -> {
                for (EquipmentSlot armorSlot : EquipmentSlot.values()) {
                    if (armorSlot.getType() == EquipmentSlot.Type.ARMOR) {
                        ItemStack armor = inventory.player.getItemBySlot(armorSlot);
                        var items = ArmorNBTHelper.getSpellTag(armor, source);
                        for (int i = 0; i < items.size(); i++) {
                            ItemStack item = ItemStack.of(items.getCompound(i));
                            if (item.isEmpty())
                                continue;
                            if (armorSlot.equals(pSelect.equippedSlot) && i == pSelect.index)
                                this.primarySelected = spells.size();
                            if (armorSlot.equals(sSelect.equippedSlot) && i == sSelect.index)
                                this.secondarySelected = spells.size();

                            spells.add(new Tuple<EquipmentSlot, ItemStack>(armorSlot, item));
                        }
                    }
                }

                if (pSelect.equippedSlot == null)
                    primarySelected = -1;
                if (sSelect.equippedSlot == null)
                    secondarySelected = -1;
            });
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
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(MineBound.MOD_ID,  "magic/" + ForgeRegistries.ITEMS.getKey(spells.get(pSlot).getB().getItem()).getPath())).getDefaultInstance();
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        if (!inventory.player.level.isClientSide()) {
            
            SelectedSpell selected = inventory.player.getCapability(cap).orElse(null);
            if (selected == null || isPrimary && pSlot == secondarySelected || !isPrimary && pSlot == primarySelected)
                return ItemStack.EMPTY;
            // TODO: Probably change this to be a bit nicer for players; the code is a mess anyway
            if (isPrimary && pSlot != primarySelected || !isPrimary && pSlot != secondarySelected) {
                ItemStack armorItem;
                ListTag equippedSpells;

                if(selected.equippedSlot != null) {
                    armorItem = inventory.player.getItemBySlot(selected.equippedSlot);
                
                    // Update old spell when removed from selected
                    equippedSpells = ArmorNBTHelper.getSpellTag(armorItem, source);
                    MagicSelectedEvent.Remove removeEvent = new MagicSelectedEvent.Remove(inventory.player, ItemStack.of(equippedSpells.getCompound(selected.index)), selected);
                    
                    if(MinecraftForge.EVENT_BUS.post(removeEvent))
                        return ItemStack.EMPTY;
                    
                    equippedSpells.set(selected.index, removeEvent.spell.serializeNBT());
                    ArmorNBTHelper.saveSpellTag(armorItem, source, equippedSpells);
                }
                // Update new spell when selecting it.
                selected.equippedSlot = spells.get(pSlot).getA();
                armorItem = inventory.player.getItemBySlot(selected.equippedSlot);
                
                equippedSpells = ArmorNBTHelper.getSpellTag(armorItem, source);
                selected.index = equippedSpells.indexOf(spells.get(pSlot).getB().serializeNBT());

                if (isPrimary)
                    primarySelected = pSlot;
                else
                    secondarySelected = pSlot;

                MagicSelectedEvent.Select selectEvent = new MagicSelectedEvent.Select(inventory.player, ItemStack.of(equippedSpells.getCompound(selected.index)), selected);
                MinecraftForge.EVENT_BUS.post(selectEvent); 

                equippedSpells.set(selected.index, selectEvent.spell.serializeNBT());
                ArmorNBTHelper.saveSpellTag(armorItem, source, equippedSpells);
                
                CapabilitySync.NET_CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) inventory.player),
                        new SelectedSpellsSync(isPrimary, selected.equippedSlot, selected.index));
            }

            inventory.player.closeContainer();
        }
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
        //refreshSpells();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
