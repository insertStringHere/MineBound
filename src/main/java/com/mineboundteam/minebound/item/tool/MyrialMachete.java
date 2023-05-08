package com.mineboundteam.minebound.item.tool;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.inventory.SelectSpellMenu;
import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import com.mineboundteam.minebound.magic.SpellItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class MyrialMachete extends SwordItem {
    TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config;

    public MyrialMachete(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);

        this.config = config;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player player) {
            player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> SpellItem.reduceMana(config.MANA_COST_PER_HIT.get(), player));
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof ServerPlayer player) {
            if (!pIsSelected || (player.containerMenu != player.inventoryMenu && !(player.containerMenu instanceof SelectSpellMenu))) {
                player.getInventory().removeItem(pStack);
            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        player.getInventory().removeItem(item);
        return false;
    }

    @SubscribeEvent
    public static void onDrop(ItemTossEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            if (event.getEntityItem().getItem().getItem() instanceof MyrialMachete) {
                event.setCanceled(true);
            }
        }
    }
}
