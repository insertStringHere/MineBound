package com.example.examplemod.item.custom;

import com.example.examplemod.sound.ModSounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlarpItem extends Item {
    public FlarpItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.playSound(ModSounds.FLARP_PLAYED_WITH.get(), 1f, 1f);
        return super.use(level, player, interactionHand);
    }
}
