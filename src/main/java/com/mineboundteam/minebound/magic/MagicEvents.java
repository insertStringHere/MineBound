package com.mineboundteam.minebound.magic;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.ArmorRecoveryProvider;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider.PlayerMana;
import com.mineboundteam.minebound.config.ArmorConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.registry.config.ArmorConfigRegistry;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class MagicEvents {
    protected static final EnumSet<EquipmentSlot> armorSlots = EnumSet.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST,
            EquipmentSlot.LEGS, EquipmentSlot.FEET);

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER  && event.player.level.getGameTime() % 10 == 0 
                && event.phase == TickEvent.Phase.START) {
            handlePlayerArmor(event.player);
            event.player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(handlePlayerMana(event.player));
        }
    }

    protected static UUID healthReductionID = new UUID(237427279, 347509);

    protected static void handlePlayerArmor(Player player) {
        var tier = new Object() {
            ArmorTier max = null;
        };

        for (ItemStack item : player.getArmorSlots()) {
            if (!item.isEmpty())
                item.getCapability(ArmorRecoveryProvider.ARMOR_RECOVERY).ifPresent(recovery -> {
                    MyrialArmorItem armorItem = (MyrialArmorItem) item.getItem();
                    if (armorItem.getDamage(item) >= armorItem.getMaxDamage(item))
                        recovery.recovering = true;
                    if (1.0d * armorItem.getDamage(item) / armorItem.getMaxDamage(item) <= 0.25d)
                        recovery.recovering = false;

                    if (tier.max == null || tier.max.getValue() < armorItem.getTier().getValue())
                        tier.max = armorItem.getTier();

                    int armorDamage = armorItem.getDamage(item);
                    if (armorDamage > 0) {
                        FoodData pFoodData = player.getFoodData();
                        if (pFoodData.getSaturationLevel() > 0 && pFoodData.getFoodLevel() >= 20) {
                            float f = Math.min(pFoodData.getSaturationLevel(), 6.0F);
                            armorItem.setDamage(item, (int) (armorDamage - f / 6));
                            pFoodData.addExhaustion(f/16.0f);
                        } else if(pFoodData.getFoodLevel() >= 16){
                            armorItem.setDamage(item, armorDamage - 1);
                            pFoodData.addExhaustion(0.125f);
                        }

                        if (armorItem.getDamage(item) < 0)
                            armorItem.setDamage(item, 0);
                    }

                });
        }

        int healthReduction = tier.max == null ? 0 : (tier.max.getValue() + 1) * -2;
        AttributeInstance healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);


        if (healthAttribute.getModifier(healthReductionID) != null
                && (int) healthAttribute.getModifier(healthReductionID).getAmount() != healthReduction) {
            healthAttribute.removeModifier(healthReductionID);
            healthAttribute.addTransientModifier(
                    new AttributeModifier(healthReductionID, "HealthReduce", healthReduction, Operation.ADDITION));
        } else if (healthAttribute.getModifier(healthReductionID) == null){
            healthAttribute.addTransientModifier(new AttributeModifier(healthReductionID, "HealthReduce", healthReduction, Operation.ADDITION));
        }

        if(player.getHealth() > player.getMaxHealth()){
            player.hurt(DamageSource.STARVE, player.getHealth()-player.getMaxHealth());
        }
    }

    protected static NonNullConsumer<PlayerMana> handlePlayerMana(Player player) {
        return mana -> {
            int manaBoost = 0;
            int totalMana = mana.getManaMax();
            int recBoost = 0;

            boolean totalArmorSet = true;
            boolean availableArmorSet = true;
            ArmorTier tier = null;
            List<ItemStack> mArmors = new ArrayList<>();

            // check each armor slot and get their bonuses to recovery and cap
            for (EquipmentSlot slot : armorSlots) {
                ItemStack armorStack = player.getItemBySlot(slot);
                Item armorItem = armorStack.getItem();

                if (armorItem instanceof MyrialArmorItem) {
                    ArmorConfig config = ((MyrialArmorItem) armorItem).getConfig();
                    totalMana += config.MANAPOOL.get();

                    var flag = new Object() {
                        boolean canUse = false;
                    };
                    armorStack.getCapability(ArmorRecoveryProvider.ARMOR_RECOVERY)
                            .ifPresent(recovery -> flag.canUse = !recovery.recovering);

                    if (flag.canUse) {
                        mArmors.add(player.getItemBySlot(slot));
                        manaBoost += config.MANAPOOL.get();
                        recBoost += config.RECOVERY.get();
                    } else {
                        availableArmorSet = false;
                    }

                    if (tier == null)
                        tier = ((MyrialArmorItem) armorItem).getTier();
                    if (tier == ((MyrialArmorItem) armorItem).getTier())
                        continue;
                }
                availableArmorSet = false;
                totalArmorSet = false;
            }

            if (totalArmorSet) {
                ArmorConfig setConfig = ArmorConfigRegistry.SET_BONUS_MAP.get(tier);
                totalMana += setConfig.MANAPOOL.get();
                if (availableArmorSet) {
                    manaBoost += setConfig.MANAPOOL.get();
                    recBoost += setConfig.RECOVERY.get();
                }
            }

            // if mana is recovered, calculate charge drained from armor durability
            if (mana.getManaMax() + manaBoost > mana.getMana()) {
                for (ItemStack stack : mArmors) {
                    if (player.getRandom().nextInt(3) == 0) {
                        stack.setDamageValue(stack.getDamageValue() + 1);
                    }
                }
            }

            mana.setTotalManaCap(totalMana);
            mana.setAvailableManaCap(mana.getManaMax() + manaBoost);
            mana.addMana(mana.getManaRecRate() + recBoost);
        };
    }
}
