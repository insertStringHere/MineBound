package com.mineboundteam.minebound.item.tool;

import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import com.mineboundteam.minebound.item.MyrialSwordPlaceholder;
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
    public MyrialSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties,
                       TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties, config);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!level.isClientSide) {
            player.setItemInHand(interactionHand, new ItemStack(ItemRegistry.MYRIAL_SWORD_PLACEHOLDER.get()));
            level.addFreshEntity(new MyrialSwordEntity(player, level, interactionHand,
                    (itemStack -> itemStack.getItem() instanceof MyrialSwordPlaceholder), this.config));
        }
        return super.use(level, player, interactionHand);
    }
}
