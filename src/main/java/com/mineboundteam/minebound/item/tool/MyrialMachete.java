package com.mineboundteam.minebound.item.tool;

import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import com.mineboundteam.minebound.magic.SpellItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class MyrialMachete extends SwordItem {

    private final int manaCost;

    public MyrialMachete(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);

        this.manaCost = config.MANA_COST_PER_HIT.get();
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player player) {
            player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> SpellItem.reduceMana(manaCost, player));
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide() && pEntity instanceof Player player) {
            if(!pIsSelected || player.containerMenu != player.inventoryMenu) {
                player.getInventory().removeItem(pStack);
            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        player.getInventory().removeItem(item);
        return false;
    }
}
