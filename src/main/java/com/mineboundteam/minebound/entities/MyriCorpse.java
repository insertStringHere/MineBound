package com.mineboundteam.minebound.entities;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeCallable;
import net.minecraft.world.entity.*;

public class MyriCorpse extends LivingEntity {

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);

    public MyriCorpse(EntityType<? extends MyriCorpse> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return armorItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean isEffectiveAi() {
        return false;
    }

    @Override
    public boolean canTakeItem(ItemStack item) {
        return false;
    }

    @Override
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity e) {
        e.setDeltaMovement(e.getDeltaMovement().multiply((double) .2f, 1.0D, (double) .2f));
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!this.level.isClientSide && !this.isRemoved()) {
            if (DamageSource.OUT_OF_WORLD.equals(source))
                this.kill();
            else if (!this.isInvulnerableTo(source)) {
                if (source.isExplosion()) {
                    this.brokenByAnything(source);
                    this.kill();
                } else {
                    if (source.getEntity() instanceof Player)
                        if (!((Player) source.getEntity()).getAbilities().mayBuild) {
                        } else if (source.isCreativePlayer()) {
                            this.playBrokenSound();
                            this.showBreakingParticles();
                            this.kill();
                        } else {
                            this.brokenByAnything(source);
                            this.showBreakingParticles();
                            this.kill();

                            return true;
                        }
                }
            }
        }
        return false;
    }

    public boolean shouldRenderAtSqrDistance(double p_31574_) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0) || d0 == 0.0D) {
            d0 = 4.0D;
        }

        d0 *= 64.0D;
        return p_31574_ < d0 * d0;
    }

    private void showBreakingParticles() {
        if (this.level instanceof ServerLevel) {
            ((ServerLevel) this.level).sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LAPIS_ORE.defaultBlockState()), this.getX(),
                    this.getY(0.6666666666666666D), this.getZ(), 10, (double) (this.getBbWidth() / 4.0F),
                    (double) (this.getBbHeight() / 4.0F), (double) (this.getBbWidth() / 4.0F), 0.05D);
        }

    }

    private void brokenByAnything(DamageSource source) {
        this.playBrokenSound();
        this.dropAllDeathLoot(source);
    }

    private void playBrokenSound() {
        this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.SLIME_BLOCK_PLACE,
                this.getSoundSource(), 1.0F, 1.0F);
    }

}
