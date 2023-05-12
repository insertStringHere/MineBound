package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.item.tool.MyrialSwordItem;
import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import com.mineboundteam.minebound.util.BoundingBoxUtil;
import com.mineboundteam.minebound.util.PlayerUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public abstract class MyrialSwordEntityBase extends ThrowableItemProjectile {
    protected InteractionHand usedHand;
    protected ItemStack swordVacuum;
    protected TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config;

    /**
     * Derived from {@link LivingEntity}
     */
    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYRot;
    protected double lerpXRot;

    public MyrialSwordEntityBase(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public MyrialSwordEntityBase(EntityType<? extends ThrowableItemProjectile> pEntityType, Player player, Level pLevel, InteractionHand usedHand, ItemStack swordVacuum, TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config) {
        super(pEntityType, player, pLevel);
        this.usedHand = usedHand;
        this.swordVacuum = swordVacuum;
        this.config = config;
        this.setPos(PlayerUtil.getHandPos(player, usedHand));
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return config.SWORD_ITEM.get();
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof LivingEntity entity && entity.hasLineOfSight(this)) {
            if (this.getOwner() != null && entity.is(this.getOwner())) {
                this.discard();
                this.switchToSword((Player) this.getOwner());
            } else {
                entity.hurt(DamageSource.playerAttack((Player) this.getOwner()), config.PROJECTILE_DMG.get().floatValue());
            }
        }
    }

    @Override
    public void tick() {
        if (this.isControlledByLocalInstance()) {
            /**
             * Derived from {@link ProjectileUtil#getHitResult(Entity, Predicate)}
             * Derived from {@link ThrowableItemProjectile#tick()}
             */
            AABB inflatedBB = this.getBoundingBox().expandTowards(this.getMoveVector()).inflate(1);
            EntityHitResult hitresult = ProjectileUtil.getEntityHitResult(this.getLevel(), this,
                    BoundingBoxUtil.vectorFromMin(inflatedBB),
                    BoundingBoxUtil.vectorFromMax(inflatedBB),
                    inflatedBB, this::canHitEntity, .75f);
            if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHitEntity(hitresult);
            }
        }

        super.tick();
        this.move();

        /**
         * Derived from {@link LivingEntity#aiStep()}
         */
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }
        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
            double d2 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
            double d4 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
            float yRot = this.getYRot() + (float) (this.lerpYRot - (double) this.getYRot()) / (float) this.lerpSteps;
            float xRot = this.getXRot() + (float) (this.lerpXRot - (double) this.getXRot()) / (float) this.lerpSteps;
            --this.lerpSteps;
            this.setPos(d0, d2, d4);
            this.setRot(yRot, xRot);
        }
    }

    /**
     * Derived from {@link LivingEntity#lerpTo(double, double, double, float, float, int, boolean)}
     */
    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.lerpX = pX;
        this.lerpY = pY;
        this.lerpZ = pZ;
        this.lerpYRot = pYaw;
        this.lerpXRot = pPitch;
        this.lerpSteps = pPosRotationIncrements;
    }

    protected void move() {
        if (this.getOwner() == null || swordVacuum == null) return;
        this.move(MoverType.PLAYER, getMoveVector());
        // TODO: Modify this when model is implemented
        this.setRot(this.getYRot() + 90, this.getOwner().getXRot());
    }

    protected Vec3 getMoveVector() {
        if (this.getOwner() == null || swordVacuum == null) return Vec3.ZERO;

        if (swordVacuum.getOrCreateTag().getBoolean(MyrialSwordItem.RETURN_KEY)) {
            return PlayerUtil.getHandPos(this.getOwner(), this.usedHand).subtract(this.position()).scale(0.3);
        }

        return PlayerUtil.getShift(this.getOwner(), this.usedHand, this, config.PROJECTILE_RANGE.get());
    }

    protected void switchToSword(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack potentialVacuum = player.getInventory().getItem(i);
            if (!potentialVacuum.isEmpty() && potentialVacuum.sameItem(new ItemStack(config.VACUUM_ITEM.get()))) {
                player.getInventory().removeItem(i, 1);
                player.getInventory().add(i, new ItemStack(config.SWORD_ITEM.get()));
                break;
            }
        }
    }
}