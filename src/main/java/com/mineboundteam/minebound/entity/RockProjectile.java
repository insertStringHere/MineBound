package com.mineboundteam.minebound.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;


import static com.mineboundteam.minebound.entity.registry.EntityRegistry.ROCK_PROJECTILE;


/**
 * Derived from {@link AbstractHurtingProjectile}
 * Derived from {@link FireProjectile}
 * Derived from {@link net.minecraft.world.entity.projectile.AbstractArrow}
 */
public class RockProjectile extends AbstractArrow {
    private double damage;
    private double originalX;
    private double originalY;
    private double originalZ;
    private int maxDist;

    public RockProjectile(EntityType<? extends RockProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setSoundEvent(SoundEvents.STONE_BREAK);
        //Overwrite damage that would have been applied in AbstractArrow
        this.setBaseDamage(0.0D);
    }

    public RockProjectile(Level pLevel, LivingEntity shooter, double pX, double pY, double pZ, double damage, int maxDistance) {
        super(ROCK_PROJECTILE.get(), pX, pY, pZ, pLevel);
        this.setOwner(shooter);

        this.damage = damage;
        this.originalX = pX;
        this.originalY = pY;
        this.originalZ = pZ;
        this.maxDist = maxDistance;
        this.setSoundEvent(SoundEvents.STONE_BREAK);
        //Overwrite damage that would have been applied in AbstractArrow
        this.setBaseDamage(0.0D);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if(!this.level.isClientSide){
            Entity target = pResult.getEntity();
            Entity player = this.getOwner();
            target.hurt(DamageSource.thrown(this, player), (float) damage);
            if (player instanceof LivingEntity p ) {
                this.doEnchantDamageEffects(p, target);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                BlockPos blockpos = pResult.getBlockPos().relative(pResult.getDirection());
                if (this.level.isEmptyBlock(blockpos)) {
                    this.level.explode(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0.5F, false, Explosion.BlockInteraction.BREAK);
                }
            }
            this.discard();
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            double dX = Math.abs(this.getX() - originalX);
            double dY = Math.abs(this.getY() - originalY);
            double dZ = Math.abs(this.getZ() - originalZ);
            if (dX + dY + dZ > maxDist) {
                this.discard();
            }
        }
    }
    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }



}