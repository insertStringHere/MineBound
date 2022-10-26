package com.mineboundteam.minebound.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SpellHolderBlockEntity extends BlockEntity {
    private int progress = 0;

    public SpellHolderBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.SPELL_HOLDER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public void tick() {
        if (level == null) return;

        progress++;
        if (progress > 100) {
            progress = 0;
            Bat bat = new Bat(EntityType.BAT, level);
            bat.setPos(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
            level.addFreshEntity(bat);
        }
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        progress = compoundTag.getInt("progress");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("progress", progress);
    }
}
