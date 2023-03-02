package com.mineboundteam.minebound.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Magelight extends Block {
    public Magelight(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onPlace(BlockState newBlockState, Level level, @NotNull BlockPos blockPos, @NotNull BlockState oldBlockState, boolean isMoving) {
        level.scheduleTick(blockPos, newBlockState.getBlock(), 60);
    }

    @Override
    public void tick(@NotNull BlockState blockState, ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull Random random) {
        serverLevel.destroyBlock(blockPos, false);
    }
}
