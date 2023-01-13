package com.mineboundteam.minebound.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.*;

/**
 * Created based off {@link net.minecraft.world.entity.decoration.ArmorStand},
 * creates a static My'ri model to emulate a block
 */
public class MyriCorpse extends LivingEntity {

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);

    public MyriCorpse(EntityType<? extends MyriCorpse> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

// Ignore these next few; they're necessary overrides but unused
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

    // Make the model non-ai
    @Override
    public boolean isEffectiveAi() {
        return false;
    }

    // Cannot pick up items
    @Override
    public boolean canTakeItem(ItemStack item) {
        return false;
    }

    // Remove the entity on kill
    @Override
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
    }

    // Cannot be moved by mobs
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    /**
     * Gets called whenever an entity collides with this one. Instead of doing the default push, entities can 
     * move through, but get a speed modifier of .2 and occasionally plays a slime block sound
     * @param e - The {@link Entity} interacting with this one.
     */
    protected void doPush(Entity e) {
        e.setDeltaMovement(e.getDeltaMovement().multiply((double) .2f, 1.0D, (double) .2f));
        if(level.getGameTime() % 25 == 0 && random.nextInt(2) == 0){
            level.playLocalSound(getX(), getY(), getZ(), random.nextInt(2) == 1 ? SoundEvents.SLIME_BLOCK_STEP : SoundEvents.SLIME_BLOCK_FALL, SoundSource.BLOCKS, 1, 1, false);
        }
    }

    @Override
    /**
     * Gets called whenever damage would be dealt to the entity.
     * @param source - a {@link DamageSource} to indicate where the damage came from
     * @param damage - assumeably the amount of damage dealt
     */
    public boolean hurt(DamageSource source, float damage) {
        // If on the server
        if (!this.level.isClientSide && !this.isRemoved()) {
            // if falling into void, just kill it
            if (DamageSource.OUT_OF_WORLD.equals(source))
                this.kill();
            // if it's not a damage source that this entity is invulnerable to
            else if (!this.isInvulnerableTo(source)) {
                // if it's an explosion, destroy it but also do the drops
                if (source.isExplosion()) {
                    this.brokenByAnything(source);
                    this.kill();
                // otherwise, only destroy if it's a player
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

    // Displays breaking particles like when breaking a block.
    private void showBreakingParticles() {
        if (this.level instanceof ServerLevel) {
            ((ServerLevel) this.level).sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LAPIS_ORE.defaultBlockState()), this.getX(),
                    this.getY(0.6666666666666666D), this.getZ(), 10, (double) (this.getBbWidth() / 4.0F),
                    (double) (this.getBbHeight() / 4.0F), (double) (this.getBbWidth() / 4.0F), 0.05D);
        }

    }

    // Plays broken sound and drops loot
    private void brokenByAnything(DamageSource source) {
        this.playBrokenSound();
        this.dropAllDeathLoot(source);
    }

    // Plays a sound when the block is broken 
    private void playBrokenSound() {
        this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), SoundEvents.SLIME_BLOCK_BREAK,
                this.getSoundSource(), 1.0F, 1.0F);
    }

}
