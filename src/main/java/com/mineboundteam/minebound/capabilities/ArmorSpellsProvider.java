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
    public static Capability<SpellContainer> ARMOR_ACTIVE_SPELLS = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static Capability<SpellContainer> ARMOR_PASSIVE_SPELLS = CapabilityManager.get(new CapabilityToken<>() {
    });

    protected final LazyOptional<SpellContainer> optional = LazyOptional.of(this::createSpellSlots);

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

    protected SpellContainer createSpellSlots() {
        if (this.spells == null)
            this.spells = new SpellContainer();

        return this.spells;
    }

    public class SpellContainer {
        public NonNullList<ItemStack> items;

        public void saveNBT(CompoundTag nbt) {
            for (int i = 0; i < items.size(); i++)
                nbt.put(Integer.toString(i), items.get(i).getItem().getShareTag(items.get(i)));
        }

        public void loadNBTData(CompoundTag nbt) {
            for (int i = 0; i < nbt.size(); i++) {
                ItemStack stack = ItemStack.EMPTY;
                stack.getItem().readShareTag(stack, nbt.getCompound(Integer.toString(i)));
                items.add(stack);
            }
        }
    }

    public static class ArmorActiveSpellsProvider extends ArmorSpellsProvider {
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ARMOR_ACTIVE_SPELLS ? optional.cast() : LazyOptional.empty();
        }
    }

    public static class ArmorPassiveSpellsProvider extends ArmorSpellsProvider {
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ARMOR_ACTIVE_SPELLS ? optional.cast() : LazyOptional.empty();
        }
    }

}
