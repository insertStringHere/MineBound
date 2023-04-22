package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.entity.registry.EntityRegistry;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MyrialSwordEntity extends ThrowableItemProjectile {
    int maxDistance = 10;
    ItemStack myrialSwordVacuum;

    public MyrialSwordEntity(EntityType<MyrialSwordEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MyrialSwordEntity(Player player, Level level, ItemStack myrialSwordVacuum) {
        super(EntityRegistry.MYRIAL_SWORD_ENTITY.get(), player, level);
        this.myrialSwordVacuum = myrialSwordVacuum;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegistry.MYRIAL_SWORD.get();
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void onInsideBlock(@NotNull BlockState blockState) {
        if (myrialSwordVacuum != null && !blockState.is(Blocks.AIR))
            myrialSwordVacuum.getOrCreateTag().putBoolean("minebound.return_myrial_sword", true);
    }

    @Override
    public void tick() {
        move();
        checkInsideEntity();
        checkInsideBlocks();
        super.tick();
    }

    // helper methods
    public void move() {
        setYRot(getYRot() + 100);
        if (getOwner() == null || myrialSwordVacuum == null) return;

        if (myrialSwordVacuum.getOrCreateTag().getBoolean("minebound.return_myrial_sword")) {
            setDeltaMovement(getDeltaMovement().scale(.9).add(getOwner().getEyePosition().subtract(position()).normalize().scale(.4)));
        } else if (position().distanceTo(getOwner().getEyePosition()) > maxDistance - 1) { // smooth hovering motion
            setDeltaMovement(Vec3.ZERO);
            moveTo(getOwner().getEyePosition().add(getOwner().getLookAngle().scale(maxDistance)));
        } else { // chaotic homing motion
            setDeltaMovement(getDeltaMovement().scale(.9).add(getOwner().getEyePosition().add(getOwner().getLookAngle().scale(15)).subtract(position()).normalize().scale(.4)));
        }
    }

    public void checkInsideEntity() {
        for (Entity entity : level.getEntities(this, getBoundingBox(), this::canHitEntity)) {
            if (getOwner() != null && entity.is(getOwner())) {
                discard();
                switchToMyrialSword((Player) getOwner());
            } else {
                entity.hurt(DamageSource.GENERIC, 7);
            }
        }
    }

    public void switchToMyrialSword(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack potentialVacuum = player.getInventory().getItem(i);
            if (!potentialVacuum.isEmpty() && potentialVacuum.sameItem(new ItemStack(ItemRegistry.MYRIAL_SWORD_VACUUM.get()))) {
                player.getInventory().removeItem(i, 1);
                player.getInventory().add(i, new ItemStack(ItemRegistry.MYRIAL_SWORD.get()));
                break;
            }
        }
    }
}
