package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.item.tool.MyrialSwordItem;
import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import com.mineboundteam.minebound.util.EntityUtil;
import com.mineboundteam.minebound.util.PlayerUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public abstract class MyrialSwordEntityBase extends ThrowableItemProjectile {
    protected InteractionHand usedHand;
    protected Predicate<ItemStack> isPlaceholder;
    protected ItemStack swordPlaceholder;
    protected TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config;
    protected int invSlot;

    /**
     * Derived from {@link LivingEntity}
     */
    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;

    public MyrialSwordEntityBase(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public MyrialSwordEntityBase(EntityType<? extends ThrowableItemProjectile> pEntityType, Player player, Level pLevel,
                                 InteractionHand usedHand, Predicate<ItemStack> isPlaceholder, TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config) {
        super(pEntityType, player, pLevel);
        this.usedHand = usedHand;
        this.isPlaceholder = isPlaceholder;
        this.swordPlaceholder = player.getItemInHand(usedHand);
        this.config = config;
        this.invSlot = player.getInventory().selected;
        this.setPos(PlayerUtil.getHandPos(player, usedHand));
        this.setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return config == null ? Items.AIR : config.SWORD_ITEM.get();
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
                this.getPlayerOwner().getInventory().setItem(invSlot, config.SWORD_ITEM.get().getDefaultInstance());
            } else {
                entity.hurt(DamageSource.playerAttack(this.getPlayerOwner()), config.PROJECTILE_DMG.get().floatValue());
            }
        }
    }

    @Override
    public void tick() {
        if (this.isControlledByLocalInstance()) {
            // Need to do this so if the placeholder is dropped from the inventory it comes back since setting the NBT
            // data from ItemTossEvent was not working correctly
            if (this.getPlayerOwner() != null && usedHand != null) {
                this.swordPlaceholder = this.getPlayerOwner().getItemInHand(usedHand);
            }

            for (EntityHitResult entityHitResult : EntityUtil.multiEntityHitResult(this.getLevel(), this,
                    this.getBoundingBox().expandTowards(this.getMoveVector()), this::canHitEntity, 0)) {
                if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, entityHitResult)) {
                    this.onHitEntity(entityHitResult);
                }
            }
        }

        super.tick();
        this.move();
        if (this.getOwner() != null) {
            this.setRot(this.getOwner().getYRot(), this.getOwner().getXRot());
        }

        /**
         * Derived from {@link LivingEntity#aiStep()}
         */
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }
        if (this.lerpSteps > 0) {
            double dX = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
            double dY = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
            double dZ = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
            --this.lerpSteps;
            this.setPos(dX, dY, dZ);
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
        this.lerpSteps = pPosRotationIncrements;
    }

    protected Player getPlayerOwner() {
        return (Player) this.getOwner();
    }

    protected void move() {
        if (this.getOwner() == null || swordPlaceholder == null) return;

        this.move(MoverType.PLAYER, getMoveVector());
    }

    protected Vec3 getMoveVector() {
        if (this.getOwner() == null || swordPlaceholder == null) return Vec3.ZERO;

        if (!isPlaceholder.test(swordPlaceholder) || swordPlaceholder.getOrCreateTag().getBoolean(MyrialSwordItem.RETURN_KEY)) {
            return PlayerUtil.getHandPos(this.getPlayerOwner(), this.usedHand).subtract(this.position()).scale(0.3);
        }

        return PlayerUtil.getShift(this.getPlayerOwner(), this.usedHand, this, config.PROJECTILE_RANGE.get());
    }
}
