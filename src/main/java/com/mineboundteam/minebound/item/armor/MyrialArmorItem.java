package com.mineboundteam.minebound.item.armor;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.ArmorConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import com.mineboundteam.minebound.config.ManaConfig;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class MyrialArmorItem extends GeoArmorItem implements IAnimatable {
    private final ArmorConfig config;
    private final ArmorTier tier;
    public static final ChatFormatting[] tierColors = new ChatFormatting[]{
            ChatFormatting.YELLOW,
            ChatFormatting.GOLD,
            ChatFormatting.DARK_RED,
            ChatFormatting.DARK_AQUA
    };

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public MyrialArmorItem(ArmorMaterial pMaterial, EquipmentSlot pSlot, Properties pProperties, ArmorTier pTier, ArmorConfig pConfig) {
        super(pMaterial, pSlot, pProperties.durability(pConfig.ENERGY.get()));
        this.config = pConfig;
        this.tier = pTier;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        // Remove normal damage, we're not using durability for durability
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent("Tier ").withStyle(tierColors[tier.getValue()])
                                       .append(new TranslatableComponent("tooltip." + MineBound.MOD_ID + ".level." + tier.getValue()).withStyle(tierColors[tier.getValue()])));
        pTooltipComponents.add(new TextComponent("Manapool: ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent("+" + config.MANAPOOL.get()).withStyle(ChatFormatting.BLUE)));
        if (config.RECOVERY.get() > 0) {
            pTooltipComponents.add(new TextComponent("Mana Recovery: ").withStyle(ChatFormatting.GRAY)
                                           .append(new TextComponent("+" + config.RECOVERY.get() + "/sec").withStyle(ChatFormatting.BLUE)));
        }
        pTooltipComponents.add(new TextComponent("Active Magic slots: ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(config.STORAGE_SLOTS.get() + "").withStyle(ChatFormatting.RED)));
        pTooltipComponents.add(new TextComponent("Utility Magic slots: ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent(config.UTILITY_SLOTS.get() + "").withStyle(ChatFormatting.DARK_PURPLE)));
        pTooltipComponents.add(new TextComponent("Energy: ").withStyle(ChatFormatting.GRAY)
                                       .append(new TextComponent((config.ENERGY.get() - this.getDamage(pStack)) + "").withStyle(Style.EMPTY.withColor(pStack.getBarColor())))
                                       .append(" / ")
                                       .append(new TextComponent(config.ENERGY.get() + "").withStyle(ChatFormatting.GREEN)));
    }

    public ArmorConfig getConfig() {
        return config;
    }

    public ArmorTier getTier() {
        return tier;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<MyrialArmorItem>(this, getDescriptionId(), 20, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(ManaConfig.keepArmor.get() && event.isWasDeath())
            for(ItemStack item : event.getOriginal().getArmorSlots())
                if(!item.isEmpty() && item.getItem() instanceof MyrialArmorItem)
                    event.getPlayer().setItemSlot(Player.getEquipmentSlotForItem(item), item);
    }
}
