package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.item.MyrialSword;
import com.mineboundteam.minebound.registry.EntityRegistry;
import com.mineboundteam.minebound.registry.ItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
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
        Entity entity = entityHitResult.getEntity();
        if (getOwner() == null || myrialSword == null) return;

        if (entity.is(getOwner())) {
            discard();
            myrialSword.state = 0;
        } else {
            entity.hurt(DamageSource.MAGIC, 7);
        }
    }

    @Override
    public void tick() {
        setYRot(getYRot() + 100);
        super.tick();
        if (myrialSword == null || getOwner() == null) return;

        if (getOwner().position().distanceTo(position()) > 15)
            setDeltaMovement(Vec3.ZERO);
        if (myrialSword.state == 2)
            setDeltaMovement(getDeltaMovement().scale(.95).add(getOwner().position().subtract(position()).normalize().scale(.5)));
    }
}
