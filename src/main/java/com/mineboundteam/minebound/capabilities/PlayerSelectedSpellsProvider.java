package com.mineboundteam.minebound.capabilities;

import com.mineboundteam.minebound.config.ManaConfig;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerSelectedSpellsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<SelectedSpell> PRIMARY_SPELL = CapabilityManager.get(new CapabilityToken<>() { });
    public static Capability<SelectedSpell> SECONDARY_SPELL = CapabilityManager.get(new CapabilityToken<>() { });

    public static class PrimarySpellProvider extends PlayerSelectedSpellsProvider {}
    public static class SecondarySpellProvider extends PlayerSelectedSpellsProvider {}

    private SelectedSpell spell = null;
    private final LazyOptional<SelectedSpell> optional = LazyOptional.of(this::createSpell);

    private SelectedSpell createSpell() {
        if(this.spell == null)
            this.spell = new SelectedSpell(null, 0);

        return this.spell;
    }

    public static void UpdatePlayerSync(Player oldPlayer, Player newPlayer, Capability<SelectedSpell> cap){
        if(ManaConfig.keepArmor.get())
            oldPlayer.getCapability(cap).ifPresent(oldStore ->
                newPlayer.getCapability(cap).ifPresent(newStore -> {
                    newStore.equippedSlot = oldStore.equippedSlot;
                    newStore.index = oldStore.index;
                    }
                )
            );
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == PRIMARY_SPELL || cap == SECONDARY_SPELL ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createSpell().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createSpell().loadNBTData(nbt);
    } 

    public static class SelectedSpell{
        public EquipmentSlot equippedSlot;
        public int index;

        public SelectedSpell(EquipmentSlot mySlot, int index){
            this.equippedSlot = mySlot;
            this.index = index;
        }

        public void saveNBTData(CompoundTag nbt){
            nbt.putInt("slot", equippedSlot.getIndex());
            nbt.putInt("index", index);
        }
    
        public void loadNBTData(CompoundTag nbt){
            equippedSlot = EquipmentSlot.byTypeAndIndex(Type.ARMOR, nbt.getInt("slot"));
            index = nbt.getInt("index");
        }

        public boolean isEmpty(){
            return equippedSlot == null;
        }
    }    
}
