package com.mineboundteam.minebound.capabilities;

import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.PrimarySpellProvider.PrimarySelected;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SecondarySpellProvider.SecondarySelected;
import com.mineboundteam.minebound.config.ManaConfig;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public abstract class PlayerSelectedSpellsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PrimarySelected> PRIMARY_SPELL = CapabilityManager.get(new CapabilityToken<>() { });
    public static Capability<SecondarySelected> SECONDARY_SPELL = CapabilityManager.get(new CapabilityToken<>() { });


    protected SelectedSpell spell = null;
    protected final LazyOptional<? extends SelectedSpell> optional = LazyOptional.of(this::createSpell);

    protected abstract SelectedSpell createSpell();

    public static void UpdatePlayerSync(Player oldPlayer, Player newPlayer, Capability<? extends SelectedSpell> cap){
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
        public InteractionHand usedHand;
        public int index = -1;

        public SelectedSpell(EquipmentSlot mySlot, InteractionHand usedHand, int index){
            this.equippedSlot = mySlot;
            this.usedHand = usedHand;
            this.index = index;
        }

        public void saveNBTData(CompoundTag nbt){
            if(this.equippedSlot == null)
                nbt.putInt("slot", -1);
            else
                nbt.putInt("slot", equippedSlot.getIndex());
            nbt.putInt("index", index);
        }

        public void loadNBTData(CompoundTag nbt){
            index = nbt.getInt("slot");
            if(index != -1)
                equippedSlot = EquipmentSlot.byTypeAndIndex(Type.ARMOR, index);
            index = nbt.getInt("index");
        }

        public boolean isEmpty(){
            return equippedSlot == null;
        }
    }

    public static class PrimarySpellProvider extends PlayerSelectedSpellsProvider {
        public static class PrimarySelected extends SelectedSpell{ public PrimarySelected(EquipmentSlot mySlot, int index) { super(mySlot, InteractionHand.MAIN_HAND, index); }}
        @Override
        protected SelectedSpell createSpell() {
            if(this.spell == null)
                this.spell = new PrimarySelected(null, 0);
            return this.spell;
        }
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == PRIMARY_SPELL ? optional.cast() : LazyOptional.empty();
        }
    }
    public static class SecondarySpellProvider extends PlayerSelectedSpellsProvider {
        public static class SecondarySelected extends SelectedSpell{ public SecondarySelected(EquipmentSlot mySlot, int index) { super(mySlot, InteractionHand.OFF_HAND, index); }}
        @Override
        protected SelectedSpell createSpell() {
            if(this.spell == null)
                this.spell = new SecondarySelected(null, 0);
            return this.spell;
        }
        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == SECONDARY_SPELL ? optional.cast() : LazyOptional.empty();
        }
    }
}
