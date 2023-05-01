package com.mineboundteam.minebound.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;


import java.util.List;

import static com.mineboundteam.minebound.entity.registry.EntityRegistry.ROCK_PROJECTILE;


/**
 * Derived from {@link FireProjectile}
 * Derived from {@link net.minecraft.world.entity.projectile.AbstractArrow}
 */
public class RockProjectile extends AbstractArrow {
    private float damage;
    private float damageRadius;
    public RockProjectile(EntityType<? extends RockProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RockProjectile(Level pLevel, LivingEntity shooter, double pX, double pY, double pZ, float damage, float damageRadius) {
        super(ROCK_PROJECTILE.get(), pX, pY, pZ, pLevel);
        this.setOwner(shooter);

        this.damage = damage;
        this.damageRadius = damageRadius;
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
            target.hurt(DamageSource.thrown(this, player), damage);
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
                    List<Entity> hitEntities = this.level.getEntities(this, this.getBoundingBox().inflate(damageRadius));
                    for (Entity target : hitEntities) {
                        if (target instanceof LivingEntity) {
                            target.hurt(DamageSource.thrown(this, entity), damage);
                            if (entity instanceof LivingEntity p) {
                                this.doEnchantDamageEffects(p, target);
                            }
                        }
                    }
                }
            }
            this.discard();
        }
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
        for(int i = 0; i < 4; ++i) {
            this.level.addParticle(ParticleTypes.ASH, this.getX() + deltaX * (double)i / 4.0D, this.getY() + deltaY * (double)i / 4.0D, this.getZ() + deltaZ * (double)i / 4.0D, -deltaX, -deltaY + 0.2D, -deltaZ);
        }
    }
    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }



}