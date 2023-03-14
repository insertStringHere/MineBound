package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.item.MyrialSword;
import com.mineboundteam.minebound.registry.EntityRegistry;
import com.mineboundteam.minebound.registry.ItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class MyrialSwordEntity extends ThrowableItemProjectile {
    MyrialSword myrialSword;

    public MyrialSwordEntity(EntityType<MyrialSwordEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MyrialSwordEntity(Level level, Player shooter, MyrialSword myrialSword) {
        super(EntityRegistry.MYRIAL_SWORD_ENTITY.get(), shooter, level);
        this.myrialSword = myrialSword;
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
    public void onHitEntity(EntityHitResult entityHitResult) {
        Entity owner = this.getOwner();
        Entity projectile = level.getEntity(this.getId());
        Entity target = entityHitResult.getEntity();
        if (owner == null || projectile == null || myrialSword == null) return;

        if (target.is(owner)) {
            projectile.kill();
            myrialSword.state = 0;
        } else {
            target.hurt(DamageSource.MAGIC, 99);
        }
    }

    @Override
    public void tick() {
        if (myrialSword != null && getOwner() != null && myrialSword.state == 2)
            setDeltaMovement(getDeltaMovement().scale(.95).add(getOwner().position().subtract(position()).normalize().scale(.2)));
        super.tick();
    }
}
