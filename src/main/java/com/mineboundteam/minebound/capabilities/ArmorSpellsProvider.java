package com.mineboundteam.minebound.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public abstract class ArmorSpellsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ArmorActiveSpellsProvider.ActiveSpellContainer> ARMOR_ACTIVE_SPELLS = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static Capability<ArmorPassiveSpellsProvider.PassiveSpellContainer> ARMOR_PASSIVE_SPELLS = CapabilityManager.get(new CapabilityToken<>() {
    });

    protected final LazyOptional<? extends SpellContainer> optional = LazyOptional.of(this::createSpellSlots);

    protected SpellContainer spells;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createSpellSlots().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createSpellSlots().loadNBTData(nbt);
    }

    protected abstract SpellContainer createSpellSlots();

    public abstract static class SpellContainer {
        public NonNullList<ItemStack> items = NonNullList.create();

        public void saveNBT(CompoundTag nbt) {
            for (int i = 0; i < items.size(); i++)
                nbt.put(Integer.toString(i), items.get(i).serializeNBT());
        }

        public void loadNBTData(CompoundTag nbt) {
            for (int i = 0; i < nbt.size(); i++) {
                items.add(ItemStack.of(nbt.getCompound(Integer.toString(i))));
            }
        };
    }

    public static class ArmorActiveSpellsProvider extends ArmorSpellsProvider {
        public static class ActiveSpellContainer extends SpellContainer{}

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ARMOR_ACTIVE_SPELLS ? optional.cast() : LazyOptional.empty();
        }

        @Override
        protected SpellContainer createSpellSlots() {
            {
                if (this.spells == null)
                    this.spells = new ActiveSpellContainer();
        
                return this.spells;
            }
        }
    }

    public static class ArmorPassiveSpellsProvider extends ArmorSpellsProvider {
        public static class PassiveSpellContainer extends SpellContainer{}

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ARMOR_PASSIVE_SPELLS ? optional.cast() : LazyOptional.empty();
        }

        @Override
        protected SpellContainer createSpellSlots() {
            {
                if (this.spells == null)
                    this.spells = new PassiveSpellContainer();
        
                return this.spells;
            }
        }
    }

}
