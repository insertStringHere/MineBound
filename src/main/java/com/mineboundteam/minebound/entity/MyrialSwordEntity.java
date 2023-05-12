package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.entity.registry.EntityRegistry;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MyrialSwordEntity extends ThrowableItemProjectile {
    ItemStack myrialSwordVacuum;

    /**
     * Derived from {@link LivingEntity}
     */
    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYRot;
    protected double lerpXRot;

    public MyrialSwordEntity(EntityType<MyrialSwordEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MyrialSwordEntity(Player player, Level level, ItemStack myrialSwordVacuum) {
        super(EntityRegistry.MYRIAL_SWORD_ENTITY.get(), player, level);
        this.myrialSwordVacuum = myrialSwordVacuum;
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.MYRIAL_SWORD.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        System.out.println("onHitEntity");
        if (pResult.getEntity() instanceof LivingEntity entity) {
            if (this.getOwner() != null && entity.is(this.getOwner())) {
                this.discard();
                switchToMyrialSword((Player) this.getOwner());
            } else {
                entity.hurt(DamageSource.playerAttack((Player) this.getOwner()), 7);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        System.out.println(hitresult.getType());

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

        move();
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

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    // helper methods
    public void move() {
        if (this.getOwner() == null || myrialSwordVacuum == null) return;

        if (myrialSwordVacuum.getOrCreateTag().getBoolean("minebound.return_myrial_sword")) {
            this.move(MoverType.PLAYER, this.getOwner().getPosition(1).subtract(this.position()));
        } else {
            this.move(MoverType.PLAYER, getShift((Player) this.getOwner(), InteractionHand.MAIN_HAND, this));
        }
        // TODO: Modify this when model is implemented
        this.setRot(this.getYRot() + 90, this.getOwner().getXRot());
    }

    protected Vec3 getShift(Player player, InteractionHand usedHand, Entity entity) {
        Vec3 view = player.getLookAngle();

        float yRot = player.getYRot();
        double x = 0 - Math.sin(yRot * Math.PI / 180f);
        double z = Math.cos(yRot * Math.PI / 180f);
        int hand = usedHand == InteractionHand.MAIN_HAND ? 1 : -1;
        Vec3 playerPos = new Vec3(
                player.getX() - (z / 2.5d * hand),
                player.getEyeY() - 1d,
                player.getZ() + (x / 2.5d * hand)
        );
        return view.scale(10).add(playerPos).subtract(entity.position());
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
