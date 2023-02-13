package com.mineboundteam.minebound.capabilities.registry;

import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.ArmorRecoveryProvider;
import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.ArmorRecoveryProvider.ArmorRecovery;
import com.mineboundteam.minebound.capabilities.ArmorSpellsProvider.SpellContainer;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider.PlayerMana;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class CapabilityRegistry {
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
            event.addCapability(new ResourceLocation(MineBound.MOD_ID, "recovering"), new ArmorRecoveryProvider());
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
        event.register(ArmorRecovery.class);
    }
}
