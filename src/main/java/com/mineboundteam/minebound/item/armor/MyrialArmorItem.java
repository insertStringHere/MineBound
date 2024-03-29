package com.mineboundteam.minebound.item.armor;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.ArmorConfig;
import com.mineboundteam.minebound.util.ColorUtil;
import com.mineboundteam.minebound.util.TooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class MyrialArmorItem extends GeoArmorItem implements IAnimatable {
    private final ArmorConfig config;
    private final ArmorTier tier;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public static final String RECOVERY_TAG = "recovering";

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
        pTooltipComponents.add(new TextComponent("Tier ").withStyle(ColorUtil.Tooltip.armorTierColors.get(tier))
                .append(TooltipUtil.level(tier.getValue())));
        pTooltipComponents.add(new TextComponent("Manapool: ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent("+" + config.MANAPOOL.get()).withStyle(ColorUtil.Tooltip.manaColorStyle)));
        if (config.RECOVERY.get() > 0) {
            pTooltipComponents.add(new TextComponent("Mana Recovery: ").withStyle(ColorUtil.Tooltip.defaultColor)
                    .append(new TextComponent("+" + config.RECOVERY.get() + "/sec").withStyle(ColorUtil.Tooltip.manaColorStyle)));
        }
        pTooltipComponents.add(new TextComponent("Active Magic slots: ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(config.STORAGE_SLOTS.get().toString()).withStyle(ChatFormatting.RED)));
        pTooltipComponents.add(new TextComponent("Utility Magic slots: ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(config.UTILITY_SLOTS.get().toString()).withStyle(ColorUtil.Tooltip.utilityColor)));
        pTooltipComponents.add(new TextComponent("Energy: ").withStyle(ColorUtil.Tooltip.defaultColor)
                .append(new TextComponent(String.valueOf(config.ENERGY.get() - this.getDamage(pStack))).withStyle(Style.EMPTY.withColor(pStack.getBarColor())))
                .append(" / ")
                .append(new TextComponent(config.ENERGY.get().toString()).withStyle(ChatFormatting.GREEN)));
        // For spacing
        pTooltipComponents.add(new TextComponent(""));
    }

    public ArmorConfig getConfig() {
        return config;
    }

    public ArmorTier getTier() {
        return tier;
    }

    // Return true if elytra flight is possible with this armor piece, is only called on chest piece
    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return stack.getOrCreateTag().getBoolean("minebound.telekinetic_utility.elytra_flight");
    }

    // No extra processing needs to be done, so just return true
    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
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

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        // If not equipped in armor slots
        if (pSlotId >= 4) {
            EnchantmentHelper.setEnchantments(Collections.emptyMap(), pStack);
        }
    }

    @SubscribeEvent
    public static void onDrop(ItemTossEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            ItemStack itemStack = event.getEntityItem().getItem();
            if (itemStack.getItem() instanceof MyrialArmorItem) {
                EnchantmentHelper.setEnchantments(Collections.emptyMap(), itemStack);
            }
        }
    }
}
