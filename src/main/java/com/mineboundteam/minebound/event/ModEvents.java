package com.mineboundteam.minebound.event;

import com.mineboundteam.minebound.config.ArmorConfig;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.registry.config.ArmorConfigRegistry;
import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider.SpellContainer;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider.PlayerMana;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import java.util.*;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player){
            if(!event.getObject().getCapability(PlayerManaProvider.PLAYER_MANA).isPresent()){
                event.addCapability(new ResourceLocation(MineBound.MOD_ID, "properties"), new PlayerManaProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesItems(AttachCapabilitiesEvent<ItemStack> event){
        if(event.getObject().getItem() instanceof MyrialArmorItem){
            event.addCapability(new ResourceLocation(MineBound.MOD_ID, "active_spells"), new ArmorSpellsProvider.ArmorActiveSpellsProvider());
                event.addCapability(new ResourceLocation(MineBound.MOD_ID, "passive_spells"), new ArmorSpellsProvider.ArmorPassiveSpellsProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        if(event.isWasDeath()){
            event.getOriginal().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(oldStore ->
                event.getOriginal().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(newStore ->
                    newStore.copyFrom(oldStore)
                )
            );
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        event.register(PlayerMana.class);
        event.register(SpellContainer.class);
    }

    protected static final EnumSet<EquipmentSlot> armorSlots = EnumSet.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER && event.player.level.getDayTime() % 10 == 0 && event.phase == TickEvent.Phase.START) {
            event.player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                int manaBoost = 0;
                int totalMana = mana.getManaMax();
                int recBoost = 0;
                Random rndm = new Random();

                boolean totalArmorSet = true;
                boolean availableArmorSet = true;
                ArmorTier tier = null;
                List<ItemStack> mArmors = new ArrayList<>();

                // check each armor slot and get their bonuses to recovery and cap
                for (EquipmentSlot slot : armorSlots) {
                    ItemStack armorStack = event.player.getItemBySlot(slot);
                    Item armorItem = armorStack.getItem();

                    if (armorItem instanceof MyrialArmorItem) {
                        ArmorConfig config = ((MyrialArmorItem) armorItem).getConfig();
                        totalMana += config.MANAPOOL.get();

                        if (armorStack.getDamageValue() < armorStack.getMaxDamage()) {
                            mArmors.add(event.player.getItemBySlot(slot));
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
                    if (availableArmorSet){
                        manaBoost += setConfig.MANAPOOL.get();
                        recBoost += setConfig.RECOVERY.get();
                    }
                }

                // if mana is recovered, calculate charge drained from armor durability
                if(mana.getManaMax() + manaBoost > mana.getMana() && rndm.nextInt(10) == 0){
                    for(ItemStack stack : mArmors){
                        if(rndm.nextInt(3) == 0){
                            stack.setDamageValue(stack.getDamageValue() + 1);
                        }
                    }
                }

                mana.setTotalManaCap(totalMana);
                mana.setAvailableManaCap(mana.getManaMax() + manaBoost);
                mana.addMana(mana.getManaRecRate() + recBoost);
            });
        }
    }
}
