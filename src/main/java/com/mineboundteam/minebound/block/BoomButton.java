package com.mineboundteam.minebound.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BoomButton extends Block {
    public BoomButton(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (level.isClientSide())
            player.sendMessage(new TextComponent("Explosion imminent; beginning countdown"), UUID.randomUUID());
        new Thread(() -> {
            try {
                for(int i = 5; i > 0; i--){
                    Thread.sleep(1000);
                    if (level.isClientSide)
                        player.sendMessage(new TextComponent(String.format("%d", i)), UUID.randomUUID());
                }

                if (!level.isClientSide()) {
                    level.explode(player, player.getX(), player.getY(), player.getZ(), 0f, true, Explosion.BlockInteraction.NONE);
                    player.kill();
                }
            } catch(InterruptedException interruptedException) {
                System.err.println("Could not complete BoomButton detonation.");
                System.err.println(interruptedException.getMessage());
            }
        }).start();
        return InteractionResult.SUCCESS;
    }
}
