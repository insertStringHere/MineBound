package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.item.armor.ArmorTier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ActiveSpellItem extends SpellItem {

    public ActiveSpellItem(Properties properties, ArmorTier level) {
        super(properties, level);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return use(player.getItemInHand(usedHand), level, player);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        releaseUsing(pStack, pLevel, (Player) pLivingEntity);
    }

    public abstract InteractionResultHolder<ItemStack> use(ItemStack stack, Level level, Player player);

    public abstract void releaseUsing(ItemStack stack, Level level, Player player);
}
