package com.mineboundteam.minebound.item;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.inventory.SelectSpellMenu;
import com.mineboundteam.minebound.item.tool.MyrialSwordItem;
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
    public ItemStack itemStack;

    public MyrialSwordPlaceholder(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, Level level, @NotNull Entity entity, int slotID, boolean isSelected) {
        if (!level.isClientSide && this.itemStack != null && entity instanceof Player player
                && (!isSelected || (player.containerMenu != player.inventoryMenu && !(player.containerMenu instanceof SelectSpellMenu)))) {
            this.itemStack.getOrCreateTag().putBoolean(MyrialSwordItem.RETURN_KEY, true);
            player.getInventory().removeItem(itemStack);
        } else if (this.itemStack == null && entity instanceof Player player) {
            player.getInventory().removeItem(itemStack);
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack itemStack, Player player) {
        if (this.itemStack != null) {
            this.itemStack.getOrCreateTag().putBoolean(MyrialSwordItem.RETURN_KEY, true);
        }
        player.getInventory().removeItem(itemStack);
        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!level.isClientSide && itemStack != null) {
            itemStack.getOrCreateTag().putBoolean(MyrialSwordItem.RETURN_KEY, true);
        }
        return super.use(level, player, interactionHand);
    }

    @SubscribeEvent
    public static void onDrop(ItemTossEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            if (event.getEntityItem().getItem().getItem() instanceof MyrialSwordPlaceholder placeholder) {
                placeholder.itemStack.getOrCreateTag().putBoolean(MyrialSwordItem.RETURN_KEY, true);
                event.setCanceled(true);
            }
        }
    }
}
