package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MyrialSword extends SwordItem {
    MyrialSwordEntity myrialSwordEntity;
    public int state = 0;

    public MyrialSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (level.isClientSide) return super.use(level, player, interactionHand);

        if (state == 0) {
            myrialSwordEntity = new MyrialSwordEntity(level, player, this);
            myrialSwordEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5f, 1);
            level.addFreshEntity(myrialSwordEntity);
            state = 1;
        } else if (state == 1) {
            state = 2;
        }

        return super.use(level, player, interactionHand);
    }
}
