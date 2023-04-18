package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.entity.registry.EntityRegistry;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MyrialSwordEntity extends ThrowableItemProjectile {
    ItemStack itemStack;

    public MyrialSwordEntity(EntityType<MyrialSwordEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MyrialSwordEntity(Level level, Player shooter, ItemStack itemStack) {
        super(EntityRegistry.MYRIAL_SWORD_ENTITY.get(), shooter, level);
        this.itemStack = itemStack;
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
        if (getOwner() == null || itemStack == null) return;

        if (entity.is(getOwner())) {
            discard();
            itemStack.getOrCreateTag().putInt("minebound.myrial_sword", 0);
        } else {
            entity.hurt(DamageSource.MAGIC, 7);
        }
    }

    @Override
    public void tick() {
        setYRot(getYRot() + 100);
        super.tick();
        if (itemStack == null || getOwner() == null) return;

        if (getOwner().position().distanceTo(position()) > 15)
            setDeltaMovement(Vec3.ZERO);
        if (itemStack.getOrCreateTag().getInt("minebound.myrial_sword") == 2)
            setDeltaMovement(getDeltaMovement().scale(.95).add(getOwner().position().subtract(position()).normalize().scale(.5)));
    }
}
