package com.mineboundteam.minebound.item.tool;

import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import com.mineboundteam.minebound.item.MyrialSwordVacuum;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MyrialSword extends MyrialSwordItem {
    public MyrialSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties, TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, config);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!level.isClientSide) {
            ItemStack myrialSwordVacuum = this.createVacuum();
            this.switchToVacuum(player, player.getItemInHand(interactionHand), myrialSwordVacuum);
            level.addFreshEntity(new MyrialSwordEntity(player, level, interactionHand, myrialSwordVacuum, this.config));
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

    public void switchToVacuum(Player player, ItemStack myrialSword, ItemStack myrialSwordVacuum) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack potentialMyrialSword = player.getInventory().getItem(i);
            if (!potentialMyrialSword.isEmpty() && potentialMyrialSword.sameItem(myrialSword)) {
                player.getInventory().removeItem(i, 1);
                player.getInventory().add(i, myrialSwordVacuum);
                break;
            }
        }
    }
}
