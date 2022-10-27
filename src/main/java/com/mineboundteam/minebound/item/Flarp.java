package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.sound.SoundRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class Flarp extends Item {
    public Flarp(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        player.playSound(SoundRegistry.FLARP_PLAYED_WITH.get(), 1f, 1f);
        return super.use(level, player, interactionHand);
    }
}
