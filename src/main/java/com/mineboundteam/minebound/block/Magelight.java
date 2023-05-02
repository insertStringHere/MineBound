package com.mineboundteam.minebound.block;

import com.mineboundteam.minebound.magic.DefensiveSpells.LightDefensiveSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Magelight extends Block {

    private final boolean shouldDestroy;
    private final int duration;
    private static final VoxelShape SHAPE = Block.box(6, 6, 6, 10, 10, 10);

    public Magelight(Properties pProperties, LightDefensiveSpell.LightDefensiveSpellConfig config) {
        super(pProperties);

        this.shouldDestroy = config.DESTROY_MAGELIGHT.get();
        this.duration = config.MAGELIGHT_DURATION.get() * 20;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void onPlace(BlockState newBlockState, Level level, @NotNull BlockPos blockPos, @NotNull BlockState oldBlockState, boolean isMoving) {
        if (shouldDestroy) {
            level.scheduleTick(blockPos, newBlockState.getBlock(), duration);
        }
    }

    @Override
    public void tick(@NotNull BlockState blockState, ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull Random random) {
        serverLevel.destroyBlock(blockPos, false);
    }
}
