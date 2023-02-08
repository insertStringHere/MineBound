package com.mineboundteam.minebound.inventory.containers;

import java.util.function.Function;

import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider.SpellContainer;
import com.mineboundteam.minebound.inventory.slots.InputArmorSlot;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.NonNullConsumer;

public abstract class ArmorSpellContainer implements Container {
    public final InputArmorSlot item;
    protected final Capability<SpellContainer> spellCap;

    protected ArmorSpellContainer(InputArmorSlot item, Capability<SpellContainer> cap) {
        this.item = item;
        spellCap = cap;
    }

    protected ItemStack getArmor(Function<MyrialArmorItem, ItemStack> consumer) {
        if (item.getItem().isEmpty())
            return ItemStack.EMPTY;
        return consumer.apply((MyrialArmorItem) item.getItem().getItem());
    }

    protected void capabilityWrapper(NonNullConsumer<SpellContainer> consumer) {
        item.getItem().getCapability(this.spellCap).ifPresent(consumer);
    }

    @Override
    public void clearContent() {
        capabilityWrapper(slots -> {
            slots.items.clear();
            this.setChanged();
        });
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        var javaisaninferiorlanguage = new Object() {
            public boolean empty = true;
        };
        capabilityWrapper(slots -> {
            for (ItemStack item : slots.items)
                if (!item.isEmpty()) {
                    javaisaninferiorlanguage.empty = false;
                    break;
                }
        });

        return javaisaninferiorlanguage.empty;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        var javaisaninferiorlanguage = new Object() {
            public ItemStack out = ItemStack.EMPTY;
        };
        capabilityWrapper(slots -> {
            if (pSlot < slots.items.size())
                javaisaninferiorlanguage.out = slots.items.get(pSlot);
        });

        return javaisaninferiorlanguage.out;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        var javaisaninferiorlanguage = new Object() {
            public ItemStack out = ItemStack.EMPTY;
        };
        capabilityWrapper(slots -> {
            javaisaninferiorlanguage.out = ContainerHelper.removeItem(slots.items, pSlot, pAmount);

            if (!javaisaninferiorlanguage.out.isEmpty())
                this.setChanged();
        });
        return javaisaninferiorlanguage.out;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        var javaisaninferiorlanguage = new Object() {
            public ItemStack out = getItem(pSlot);
        };
        if (javaisaninferiorlanguage.out.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            capabilityWrapper(slots -> {
                slots.items.set(pSlot, ItemStack.EMPTY);
            });
            return javaisaninferiorlanguage.out;
        }
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        capabilityWrapper(slots -> {
            for (int i = 0; i < pSlot - slots.items.size(); i++)
                slots.items.add(ItemStack.EMPTY);
            slots.items.set(pSlot, pStack);
        });

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return !pPlayer.level.isClientSide();
    }

    @Override
    public void setChanged() {
    }

    public static class ActiveSpell extends ArmorSpellContainer {

        public ActiveSpell(InputArmorSlot item) {
            super(item, ArmorSpellsProvider.ARMOR_ACTIVE_SPELLS);
        }

        @Override
        public void setItem(int pSlot, ItemStack pStack) {
            Item item = this.item.getItem().getItem();
            if (!(item instanceof MyrialArmorItem) ||
                    pSlot >= ((MyrialArmorItem) item).getConfig().STORAGE_SLOTS.get())
                return;
            super.setItem(pSlot, pStack);
        }
    }
    public static class PassiveSpell extends ArmorSpellContainer {

        public PassiveSpell(InputArmorSlot item) {
            super(item, ArmorSpellsProvider.ARMOR_PASSIVE_SPELLS);
        }

        @Override
        public void setItem(int pSlot, ItemStack pStack) {
            Item item = this.item.getItem().getItem();
            if (!(item instanceof MyrialArmorItem) ||
                    pSlot >= ((MyrialArmorItem) item).getConfig().UTILITY_SLOTS.get())
                return;
            super.setItem(pSlot, pStack);
        }
    }
}