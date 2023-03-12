package com.mineboundteam.minebound.inventory.containers;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider.SpellContainer;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;
import com.mineboundteam.minebound.capabilities.network.CapabilitySync;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class InventorySpellContainer implements Container {
    public final Capability<? extends SelectedSpell> cap;
    public final Capability<? extends SpellContainer> source;

    protected final Inventory inventory;
    protected final boolean isPrimary;
    protected final NonNullList<Tuple<EquipmentSlot, ItemStack>> spells = NonNullList.create();

    public int primarySelected;
    public int secondarySelected;

    public InventorySpellContainer(Inventory pInventory, Capability<? extends SelectedSpell> pCapability,
            Capability<? extends SpellContainer> pSource, boolean isPrimary) {
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
                    ItemStack armor = inventory.player.getItemBySlot(armorSlot);
                    armor.getCapability(source).ifPresent(slots -> {
                        for (int i = 0; i < slots.items.size(); i++) {
                            if (slots.items.get(i).isEmpty())
                                continue;
                            if (armorSlot.equals(pSelect.equippedSlot) && i == pSelect.index)
                                this.primarySelected = spells.size();
                            if (armorSlot.equals(sSelect.equippedSlot) && i == sSelect.index)
                                this.secondarySelected = spells.size();

                            spells.add(new Tuple<EquipmentSlot, ItemStack>(armorSlot, slots.items.get(i)));
                        }
                    });
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

            if (isPrimary && pSlot == secondarySelected || !isPrimary && pSlot == primarySelected)
                return ItemStack.EMPTY;

            if (isPrimary && pSlot != primarySelected || !isPrimary && pSlot != secondarySelected)
                inventory.player.getCapability(cap).ifPresent(selected -> {
                    selected.equippedSlot = spells.get(pSlot).getA();

                    inventory.player.getItemBySlot(selected.equippedSlot).getCapability(source).ifPresent(slots -> {
                        selected.index = slots.items.indexOf(spells.get(pSlot).getB());
                    });

                    if (isPrimary)
                        primarySelected = pSlot;
                    else
                        secondarySelected = pSlot;
                    CapabilitySync.NET_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)inventory.player),
                            new CapabilitySync.SelectedSpellsSync(isPrimary, selected.equippedSlot, selected.index));
                });

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
