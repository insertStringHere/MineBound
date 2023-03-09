package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import com.mineboundteam.minebound.registry.EntityRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MyrialSword extends SwordItem {
    public MyrialSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        MyrialSwordEntity myrialSwordEntity = new MyrialSwordEntity(EntityRegistry.MYRIAL_SWORD_ENTITY.get(), level);
        myrialSwordEntity.setPos(player.getX(), player.getY(), player.getZ());
        level.addFreshEntity(myrialSwordEntity);

        return super.use(level, player, interactionHand);
    }
}
