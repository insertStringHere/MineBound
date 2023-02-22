package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class ActiveSpellItem extends SpellItem {

    public ActiveSpellItem(Properties properties, ArmorTier level) {
        super(properties, level);
    }

    @Override
    public abstract InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand);
}
