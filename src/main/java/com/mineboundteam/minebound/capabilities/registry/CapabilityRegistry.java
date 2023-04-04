package com.mineboundteam.minebound.capabilities.registry;

import com.mineboundteam.minebound.capabilities.*;
import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider.PlayerMana;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.PrimarySpellProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SecondarySpellProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.PrimarySpellProvider.PrimarySelected;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider.SecondarySpellProvider.SecondarySelected;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class CapabilityRegistry {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerManaProvider.PLAYER_MANA).isPresent()) {
                event.addCapability(new ResourceLocation(MineBound.MOD_ID, "properties"), new PlayerManaProvider());
                event.addCapability(new ResourceLocation(MineBound.MOD_ID, "primary_spell"), new PrimarySpellProvider());
                event.addCapability(new ResourceLocation(MineBound.MOD_ID, "secondary_spell"), new SecondarySpellProvider());
                event.addCapability(new ResourceLocation(MineBound.MOD_ID, "utility_toggle"), new PlayerUtilityToggleProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        if(event.isWasDeath()){
            event.getOriginal().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(oldStore ->
                event.getPlayer().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(newStore ->
                    newStore.copyFrom(oldStore)
                )
            );

            PlayerUtilityToggleProvider.UpdatePlayerSync(event.getOriginal(), event.getPlayer());
            PlayerSelectedSpellsProvider.UpdatePlayerSync(event.getOriginal(), event.getPlayer(), PlayerSelectedSpellsProvider.PRIMARY_SPELL);
            PlayerSelectedSpellsProvider.UpdatePlayerSync(event.getOriginal(), event.getPlayer(), PlayerSelectedSpellsProvider.SECONDARY_SPELL);
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerMana.class);
        event.register(PrimarySelected.class);
        event.register(SecondarySelected.class);
        event.register(PlayerUtilityToggleProvider.UtilityToggle.class);
    }
}
