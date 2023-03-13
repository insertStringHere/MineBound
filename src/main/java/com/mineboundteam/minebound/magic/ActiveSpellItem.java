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

    public ActiveSpellItem(Properties properties, ArmorTier level) {
        super(properties, level);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        use(player.getItemInHand(usedHand), level, player);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        releaseUsing(pStack, pLevel, (Player) pLivingEntity);
    }

    public abstract void use(ItemStack stack, Level level, Player player);

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
