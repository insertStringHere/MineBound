package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MyrialSword extends SwordItem {
    MyrialSwordEntity myrialSwordEntity;
    ItemStack itemStack;

    public MyrialSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        itemStack = new ItemStack(this);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (level.isClientSide) return super.use(level, player, interactionHand);

        if (itemStack.getOrCreateTag().getInt("minebound.myrial_sword") == 0) {
            myrialSwordEntity = new MyrialSwordEntity(level, player, itemStack);
            myrialSwordEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5f, 1);
            level.addFreshEntity(myrialSwordEntity);
            itemStack.getOrCreateTag().putInt("minebound.myrial_sword", 1);
        } else if (itemStack.getOrCreateTag().getInt("minebound.myrial_sword") == 1) {
            itemStack.getOrCreateTag().putInt("minebound.myrial_sword", 2);
        }

        return super.use(level, player, interactionHand);
    }
}
