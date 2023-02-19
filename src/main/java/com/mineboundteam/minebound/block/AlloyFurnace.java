package com.mineboundteam.minebound.block;

import com.mineboundteam.minebound.block.entity.AlloyFurnaceBlockEntity;
import com.mineboundteam.minebound.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class AlloyFurnace extends AbstractFurnaceBlock {
    public AlloyFurnace(Properties properties) {
        super(properties);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockRegistry.ALLOY_FURNACE_ENTITY.get(), AlloyFurnaceBlockEntity::serverTick);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new AlloyFurnaceBlockEntity(blockPos, blockState);
    }

    @Override
    protected void openContainer(Level level, BlockPos blockPos, Player player) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof AlloyFurnaceBlockEntity) {
                NetworkHooks.openGui(((ServerPlayer) player), (AlloyFurnaceBlockEntity) blockEntity, blockPos);
            }
        }
    }

    // Copied from FurnaceBlock.java
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {
        if (pState.getValue(LIT)) {
            double soundX = (double) pPos.getX() + 0.5D;
            double soundY = (double) pPos.getY();
            double soundZ = (double) pPos.getZ() + 0.5D;
            if (pRand.nextDouble() < 0.1D) {
                pLevel.playLocalSound(soundX, soundY, soundZ, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = pState.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = pRand.nextDouble() * 0.6D - 0.3D;
            double particleX = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
            double particleY = pRand.nextDouble() * 6.0D / 16.0D;
            double particleZ = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
            pLevel.addParticle(ParticleTypes.SMOKE, soundX + particleX, soundY + particleY, soundZ + particleZ, 0.0D, 0.0D, 0.0D);
            pLevel.addParticle(ParticleTypes.FLAME, soundX + particleX, soundY + particleY, soundZ + particleZ, 0.0D, 0.0D, 0.0D);
        }
    }
}
