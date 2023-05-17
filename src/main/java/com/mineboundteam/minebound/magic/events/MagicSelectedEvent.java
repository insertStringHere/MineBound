package com.mineboundteam.minebound.magic.events;

import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SelectedSpell;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class MagicSelectedEvent extends Event {
    public ItemStack spell;
    public final SelectedSpell spellSlot;

    public final Player player; 
    public final SelectEvent type;


    public static enum SelectEvent {
        REMOVE,
        SELECT;
    }

    public MagicSelectedEvent(Player player, ItemStack spell, SelectedSpell spellSlot, SelectEvent type) {
        this.spell = spell;
        this.spellSlot = spellSlot; 
        this.player = player;
        this.type = type;
    }


    public static class Remove extends MagicSelectedEvent {
        public Remove(Player player, ItemStack spell, SelectedSpell spellSlot) {
            super(player, spell, spellSlot, SelectEvent.REMOVE);
        }
        
        @Override
        public boolean isCancelable()
        {
            return true;
        }
    }
    public static class Select extends MagicSelectedEvent {
        public Select(Player player, ItemStack spell, SelectedSpell spellSlot) {
            super(player, spell, spellSlot, SelectEvent.SELECT);
        }
    }
}
