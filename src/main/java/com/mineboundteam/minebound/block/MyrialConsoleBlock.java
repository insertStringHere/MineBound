package com.mineboundteam.minebound.block;

import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class MyrialConsoleBlock extends GlazedTerracottaBlock {

    public MyrialConsoleBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_53683_) {
        return PushReaction.NORMAL;
    }
}
