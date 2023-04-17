package com.mineboundteam.minebound.magic;

import com.mineboundteam.minebound.capabilities.ArmorNBTHelper;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;

import java.util.concurrent.atomic.AtomicReference;

public abstract class ActiveSpellItem extends SpellItem {

    public ActiveSpellItem(Properties properties, ArmorTier level, MagicType magicType, SpellType spellType) {
        super(properties, level, magicType, spellType);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        use(player.getItemInHand(usedHand), level, player);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUsingTick(ItemStack stack, LivingEntity entity, int count) {
        if (entity instanceof Player player) {
            onUsingTick(stack, player.level, player);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player player) {
            releaseUsing(pStack, pLevel, player);
        }
    }

    /**
     * Called to do cleanup on spell tags
     * <br></br>
     * Inherited Documentation: {@inheritDoc}
     */
    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player p){
        releaseUsing(item, p.level, p);
        return true;
    }

    public abstract void use(ItemStack stack, Level level, Player player);

    public abstract void onUsingTick(ItemStack stack, Level level, Player player);

    public abstract void releaseUsing(ItemStack stack, Level level, Player player);

    protected static ItemStack getSelectedSpell(Player player,
                                                Capability<? extends PlayerSelectedSpellsProvider.SelectedSpell> cap) {
        AtomicReference<ItemStack> selectedSpell = new AtomicReference<>(new ItemStack(Items.AIR));
        player.getCapability(cap).ifPresent(selected -> {
            if (!selected.isEmpty()) {
                selectedSpell.set(ItemStack.of(ArmorNBTHelper
                                                       .getSpellTag(player.getItemBySlot(selected.equippedSlot), ArmorNBTHelper.ACTIVE_SPELL)
                                                       .getCompound(selected.index)));
            }
        });
        return selectedSpell.get();
    }
}
