package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
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
        if (!level.isClientSide) {
            MyrialSwordVacuum myrialSwordVacuum = (MyrialSwordVacuum) ItemRegistry.MYRIAL_SWORD_VACUUM.get();
            ItemStack itemStack = new ItemStack(myrialSwordVacuum);
            myrialSwordVacuum.itemStack = itemStack;

            switchToVacuum(player, itemStack);
            throwItem(player, level, itemStack);
        }
        return super.use(level, player, interactionHand);
    }

    // helper methods
    public void switchToVacuum(Player player, ItemStack myrialSwordVacuum) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack potentialMyrialSword = player.getInventory().getItem(i);
            if (!potentialMyrialSword.isEmpty() && potentialMyrialSword.sameItem(new ItemStack(this))) {
                player.getInventory().removeItem(i, 1);
                player.getInventory().add(i, myrialSwordVacuum);
                break;
            }
        }
    }

    public void throwItem(Player player, Level level, ItemStack myrialSwordVacuum) {
        MyrialSwordEntity myrialSwordEntity = new MyrialSwordEntity(player, level, myrialSwordVacuum);
        myrialSwordEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5f, 1);
        level.addFreshEntity(myrialSwordEntity);
    }
}
