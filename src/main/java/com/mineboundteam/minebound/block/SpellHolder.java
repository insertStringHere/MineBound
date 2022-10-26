package com.mineboundteam.minebound.block;

import com.mineboundteam.minebound.blockentity.BlockEntityRegistry;
import com.mineboundteam.minebound.blockentity.SpellHolderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpellHolder extends ChestBlock {
    public SpellHolder(Properties p_51490_) {
        super(p_51490_, BlockEntityRegistry.SPELL_HOLDER_BLOCK_ENTITY::get);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return BlockEntityRegistry.SPELL_HOLDER_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : ($0, $1, $2, blockEntity) -> {if (blockEntity instanceof SpellHolderBlockEntity spellHolderBlockEntity) spellHolderBlockEntity.tick();};
    }
}
