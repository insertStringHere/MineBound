package com.mineboundteam.minebound.entity;

import com.mineboundteam.minebound.entity.registry.EntityRegistry;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
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
    ItemStack myrialSwordVacuum;

    public MyrialSwordEntity(EntityType<MyrialSwordEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MyrialSwordEntity(Player player, Level level, ItemStack myrialSwordVacuum) {
        super(EntityRegistry.MYRIAL_SWORD_ENTITY.get(), player, level);
        this.myrialSwordVacuum = myrialSwordVacuum;
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
    public void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        if (getOwner() != null && entityHitResult.getEntity().is(getOwner())) {
            discard();
            switchToMyrialSword((Player) getOwner());
        } else {
            entityHitResult.getEntity().hurt(DamageSource.MAGIC, 7);
        }
    }

    @Override
    public void tick() {
        if (getOwner() != null && getOwner().position().distanceTo(position()) > 15)
            setDeltaMovement(Vec3.ZERO);
        if (myrialSwordVacuum != null && getOwner() != null && myrialSwordVacuum.getOrCreateTag().getBoolean("minebound.return_myrial_sword"))
            setDeltaMovement(getDeltaMovement().scale(.95).add(getOwner().position().subtract(position()).normalize().scale(.5)));
        setYRot(getYRot() + 100);
        super.tick();
    }

    // helper methods
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