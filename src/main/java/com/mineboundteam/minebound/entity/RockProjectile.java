package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.entity.registry.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;


/**
 * Derived from {@link FireProjectile}
 * Derived from {@link net.minecraft.world.entity.projectile.AbstractArrow}
 */
public class RockProjectile extends AbstractArrow {
    private float damage;
    private float damageRadius;
    private boolean slowOnHit;
    private int slowDuration;

    public RockProjectile(EntityType<? extends RockProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RockProjectile(Level pLevel, LivingEntity shooter, double pX, double pY, double pZ, float damage, float damageRadius, boolean slowOnHit, int slowDuration) {
        super(EntityRegistry.ROCK_PROJECTILE.get(), pX, pY, pZ, pLevel);
        this.setOwner(shooter);

        this.damage = damage;
        this.damageRadius = damageRadius;
        this.slowOnHit = slowOnHit;
        this.slowDuration = slowDuration;

        this.setSoundEvent(SoundEvents.BASALT_BREAK);
        //Overwrite damage that would have been applied in AbstractArrow
        this.setBaseDamage(0.0D);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            Entity player = this.getOwner();
            List<Entity> hitEntities = this.level.getEntities(this, this.getBoundingBox().inflate(damageRadius));
            for (Entity target : hitEntities) {
                if (target instanceof LivingEntity entity && target != player) {
                    entity.hurt(DamageSource.thrown(this, player), damage);
                    if (slowOnHit) {
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slowDuration, 0), player);
                    }
                    if (player instanceof LivingEntity p) {
                        this.doEnchantDamageEffects(p, entity);
                    }
                }
            }
        }
        this.discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();

        //Add Trail
        Vec3 vec3 = this.getDeltaMovement();
        double deltaX = vec3.x;
        double deltaY = vec3.y;
        double deltaZ = vec3.z;
        for (int i = 0; i < 4; ++i) {
            this.level.addParticle(ParticleTypes.ASH, this.getX() + deltaX * (double) i / 4.0D, this.getY() + deltaY * (double) i / 4.0D, this.getZ() + deltaZ * (double) i / 4.0D, -deltaX, -deltaY + 0.2D, -deltaZ);
        }
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }
}