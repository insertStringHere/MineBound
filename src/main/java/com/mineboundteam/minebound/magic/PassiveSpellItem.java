package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.item.armor.MyrialArmorItem;

import net.minecraft.world.entity.player.Player;

public abstract class PassiveSpellItem extends SpellItem {
    protected MyrialArmorItem equippedItem;
    protected Player equippedPlayer;

    public PassiveSpellItem(Properties properties){
        super(properties);
    }

    public void updateEquipment(MyrialArmorItem item, Player player){
        equippedItem = item;
        equippedPlayer = player; 
    }
}
