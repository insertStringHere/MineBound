package com.mineboundteam.minebound.block;

import com.mineboundteam.minebound.blockentity.AlloyFurnaceBlockEntity;
import com.mineboundteam.minebound.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class AlloyFurnace extends BaseEntityBlock {
    public AlloyFurnace(Properties properties) {
        super(properties);
    }

    // block methods - michael 11/21/2022
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    // entity methods - michael 11/21/2022
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new AlloyFurnaceBlockEntity(blockPos, blockState);
    }

    @Override
    public void onRemove(@NotNull BlockState prevState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState newState, boolean isMoving) {
        if (prevState.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof AlloyFurnaceBlockEntity) {
                ((AlloyFurnaceBlockEntity) blockEntity).dropContents();
            }
        }
        super.onRemove(prevState, level, blockPos, newState, isMoving);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof AlloyFurnaceBlockEntity) {
                NetworkHooks.openGui(((ServerPlayer) player), (AlloyFurnaceBlockEntity) blockEntity, blockPos);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.ALLOY_FURNACE_BLOCK_ENTITY.get(), AlloyFurnaceBlockEntity::tick);
    }
}

// TODO         - michael tran 12/2/2022
// fix item in hand texture
// fix item in inventory texture
// add progress bar
// add next machine
// add item entity
