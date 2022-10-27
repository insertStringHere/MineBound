package com.mineboundteam.minebound.block;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.blockentity.BlockEntityRegistry;
import com.mineboundteam.minebound.blockentity.SpellHolderBlockEntity;
import com.mineboundteam.minebound.container.SpellHolderContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpellHolder extends Block implements EntityBlock {
    public SpellHolder(Properties properties) {
        super(properties);
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

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(new SimpleMenuProvider(SpellHolderContainer.getServerContainer((SpellHolderBlockEntity) level.getBlockEntity(blockPos), blockPos), new TranslatableComponent("container." + MineBound.MOD_ID + ".spell_holder")));
            return InteractionResult.CONSUME;
        }
    }
}
