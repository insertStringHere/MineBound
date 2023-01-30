package com.mineboundteam.minebound.block;

import com.mineboundteam.minebound.block.entity.AlloyFurnaceBlockEntity;
import com.mineboundteam.minebound.registry.BlockRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

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
}
