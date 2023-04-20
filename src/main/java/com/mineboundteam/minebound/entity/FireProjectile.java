package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.entity.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

/**
 * Derived from {@link SmallFireball}
 */
public class FireProjectile extends Fireball {
    private double damage;
    private boolean igniteBlocks;
    private double originalX;
    private double originalY;
    private double originalZ;
    private int maxDist;

    public FireProjectile(EntityType<? extends FireProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FireProjectile(Level pLevel, LivingEntity shooter, double pX, double pY, double pZ, double pOffsetX, double pOffsetY, double pOffsetZ, double damage, boolean igniteBlocks, int maxDistance) {
        super(EntityRegistry.FIRE_PROJECTILE.get(), pX, pY, pZ, pOffsetX, pOffsetY, pOffsetZ, pLevel);
        this.setOwner(shooter);

        this.damage = damage;
        this.igniteBlocks = igniteBlocks;
        this.originalX = pX;
        this.originalY = pY;
        this.originalZ = pZ;
        this.maxDist = maxDistance;
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level.isClientSide()) {
            Entity target = pResult.getEntity();
            if (!target.fireImmune()) {
                Entity player = this.getOwner();
                int i = target.getRemainingFireTicks();
                target.setSecondsOnFire(5);
                boolean flag = target.hurt(DamageSource.fireball(this, player), (float) damage);
                if (!flag) {
                    target.setRemainingFireTicks(i);
                } else if (player instanceof LivingEntity p) {
                    this.doEnchantDamageEffects(p, target);
                }
            }

        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level.isClientSide && igniteBlocks) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                BlockPos blockpos = pResult.getBlockPos().relative(pResult.getDirection());
                if (this.level.isEmptyBlock(blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level, blockpos));
                }
            }
        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            this.discard();
        }
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
    protected @NotNull ParticleOptions getTrailParticle() {
        return ParticleTypes.FLAME;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean isPickable() {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }
}
