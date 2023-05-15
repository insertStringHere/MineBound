package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.item.tool.MyrialSwordItem;
import com.mineboundteam.minebound.util.PlayerUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class MyrialSwordPlaceholder extends Item {
    public MyrialSwordPlaceholder(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, Level level, @NotNull Entity entity, int slotID, boolean isSelected) {
        if (entity instanceof ServerPlayer player
                && (!isSelected || !PlayerUtil.isValidDisappearingItemMenu(player.containerMenu))) {
            itemStack.getOrCreateTag().putBoolean(MyrialSwordItem.RETURN_KEY, true);
            player.getInventory().removeItem(itemStack);
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack itemStack, Player player) {
        itemStack.getOrCreateTag().putBoolean(MyrialSwordItem.RETURN_KEY, true);
        player.getInventory().removeItem(itemStack);
        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        player.getItemInHand(interactionHand).getOrCreateTag().putBoolean(MyrialSwordItem.RETURN_KEY, true);
        return super.use(level, player, interactionHand);
    }

    @SubscribeEvent
    public static void onDrop(ItemTossEvent event) {
        if (event.getEntityItem().getItem().getItem() instanceof MyrialSwordPlaceholder) {
            event.setCanceled(true);
        }
    }
}
