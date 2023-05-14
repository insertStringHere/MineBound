package com.mineboundteam.minebound.item.tool;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.magic.OffensiveSpells.TelekineticOffensiveSpell;
import com.mineboundteam.minebound.magic.SpellItem;
import com.mineboundteam.minebound.util.PlayerUtil;
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
public abstract class MyrialSwordItem extends SwordItem {
    public static final String RETURN_KEY = "minebound.return_to_player";
    protected final TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config;

    public MyrialSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, TelekineticOffensiveSpell.TelekineticOffensiveSpellConfig config) {
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
            if (!pIsSelected || !PlayerUtil.isValidDisappearingItemMenu(player.containerMenu)) {
                player.getInventory().removeItem(pStack);
            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        player.getInventory().removeItem(item);
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }

    @SubscribeEvent
    public static void onDrop(ItemTossEvent event) {
        if (event.getEntityItem().getItem().getItem() instanceof MyrialSwordItem) {
            event.setCanceled(true);
        }
    }
}
