package com.mineboundteam.minebound.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import static com.mineboundteam.minebound.entity.registry.EntityRegistry.ROCK_PROJECTILE;


/**
 * Derived from {@link AbstractHurtingProjectile}
 * Derived from {@link FireProjectile}
 */
public class RockProjectile extends AbstractHurtingProjectile {
    private double damage;
    private double originalX;
    private double originalY;
    private double originalZ;
    private int maxDist;

    public RockProjectile(EntityType<? extends RockProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RockProjectile(Level pLevel, LivingEntity shooter, double pX, double pY, double pZ, double pOffsetX, double pOffsetY, double pOffsetZ, double damage, int maxDistance) {
        super(ROCK_PROJECTILE.get(), pX, pY, pZ, pOffsetX, pOffsetY, pOffsetZ, pLevel);
        this.setOwner(shooter);

        this.damage = damage;
        this.originalX = pX;
        this.originalY = pY;
        this.originalZ = pZ;
        this.maxDist = maxDistance;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if(!this.level.isClientSide){
            Entity target = pResult.getEntity();
            Entity player = this.getOwner();
            boolean flag = target.hurt(DamageSource.thrown(this, player), (float) damage);
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
                    this.level.explode(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 1.0F, false, Explosion.BlockInteraction.BREAK);
                }
            }
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
        return (ParticleOptions) ParticleTypes.FALLING_DUST;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

}