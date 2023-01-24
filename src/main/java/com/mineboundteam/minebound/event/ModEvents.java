package com.mineboundteam.minebound.event;

import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.registry.config.ArmorConfigRegistry;
import net.minecraft.tags.ItemTags;
import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.mana.PlayerMana;
import com.mineboundteam.minebound.mana.PlayerManaProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.EnumSet;

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
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER) {
            if (event.player.level.getDayTime() % 10 == 0 && event.phase == TickEvent.Phase.START) {
                event.player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                    int manaBoost = 0;
                    int recBoost = 0;

                    boolean armorSet = true;
                    ArmorTier tier = null;

                    for (EquipmentSlot slot : EnumSet.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)) {
                        var item = event.player.getItemBySlot(slot).getItem();

                        if (item instanceof MyrialArmorItem) {
                            var config = ((MyrialArmorItem) item).getConfig();
                            manaBoost += config.MANAPOOL.get();
                            recBoost += config.RECOVERY.get();

                            if (tier == null)
                                tier = ((MyrialArmorItem) item).getTier();
                            else if (tier != ((MyrialArmorItem) item).getTier())
                                armorSet = false;
                            continue;
                        }
                        armorSet = false;
                    }

                    if (armorSet) {
                        var setConfig = ArmorConfigRegistry.SET_BONUS_MAP.get(tier);
                        manaBoost += setConfig.MANAPOOL.get();
                        recBoost += setConfig.RECOVERY.get();
                    }

                    mana.setManaCap(mana.getManaMax() + manaBoost);
                    mana.addMana(mana.getManaRecRate() + recBoost);
                    System.out.println("Mana: " + mana.getMana() + " (less than " + (mana.getManaMax() + manaBoost) + ", up by " + (mana.getManaRecRate() + recBoost) + ") at " + event.player.level.getDayTime() + " on " + event.side);
                });
            }
        }
    }
}
