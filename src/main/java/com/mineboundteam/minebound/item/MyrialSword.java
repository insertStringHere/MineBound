package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.config.registry.MagicConfigRegistry;
import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import com.mineboundteam.minebound.item.tool.MyrialMachete;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MyrialSword extends MyrialMachete {
    public MyrialSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, MagicConfigRegistry.TELEKINETIC_OFFENSIVE_1);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!level.isClientSide) {
            ItemStack myrialSwordVacuum = createVacuum();
            switchToVacuum(player, myrialSwordVacuum);
            level.addFreshEntity(new MyrialSwordEntity(player, level, myrialSwordVacuum));
        }
        return super.use(level, player, interactionHand);
    }

    // helper methods
    public ItemStack createVacuum() {
        MyrialSwordVacuum myrialSwordVacuum = (MyrialSwordVacuum) ItemRegistry.MYRIAL_SWORD_VACUUM.get();
        ItemStack itemStack = new ItemStack(myrialSwordVacuum);
        myrialSwordVacuum.itemStack = itemStack;
        return itemStack;
    }

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
}
