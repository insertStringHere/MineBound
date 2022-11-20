package com.mineboundteam.minebound.block;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.registry.BlockEntityRegistry;
import com.mineboundteam.minebound.blockentity.AlloyFurnaceBlockEntity;
import com.mineboundteam.minebound.container.SpellHolderContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AlloyFurnace extends BaseEntityBlock {
    public AlloyFurnace(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : ($0, $1, $2, blockEntity) -> {if (blockEntity instanceof AlloyFurnaceBlockEntity alloyFurnaceBlockEntity) alloyFurnaceBlockEntity.tick();};
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return BlockEntityRegistry.ALLOY_FURNACE_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(new SimpleMenuProvider(SpellHolderContainer.getServerContainer((AlloyFurnaceBlockEntity) level.getBlockEntity(blockPos), blockPos), new TranslatableComponent("container." + MineBound.MOD_ID + ".spell_holder")));
            return InteractionResult.CONSUME;
        }
    }
}
