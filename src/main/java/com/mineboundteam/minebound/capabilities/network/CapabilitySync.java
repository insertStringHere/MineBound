package com.mineboundteam.minebound.capabilities.network;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.capabilities.PlayerManaProvider;
import com.mineboundteam.minebound.capabilities.PlayerSelectedSpellsProvider;
import com.mineboundteam.minebound.capabilities.PlayerUtilityToggleProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class CapabilitySync {
    public static final SimpleChannel NET_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MineBound.MOD_ID, "capability_sync"),
            () -> "V1.1", a -> a.equals("V1.1"), a -> a.equals("V1.1"));
    public static long msgId = 0;

    public static void registerPackets(FMLCommonSetupEvent event) {
        NET_CHANNEL.registerMessage(1, SelectedSpellsSync.class, SelectedSpellsSync::encode, SelectedSpellsSync::decode,
            (msg, ctx) -> clientHandle(msg, ctx, SelectedSpellsSync::handleSelectedSpells));
        NET_CHANNEL.registerMessage(2, ManaSync.class, ManaSync::encode, ManaSync::decode,
            (msg, ctx) -> clientHandle(msg, ctx, ManaSync::handleMana));
        NET_CHANNEL.registerMessage(3, UtilitySync.class, UtilitySync::encode, UtilitySync::decode, 
            (msg, ctx) -> clientHandle(msg, ctx, UtilitySync::handleToggles));
    }


    protected static <T> void clientHandle(T msg, Supplier<NetworkEvent.Context> ctx,
            BiConsumer<T, Supplier<NetworkEvent.Context>> func) {
        ctx.get().enqueueWork(() ->
        // Make sure it's only executed on the physical client
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> func.accept(msg, ctx)));
        ctx.get().setPacketHandled(true);
    }

    @SubscribeEvent
    public static void onConnection(PlayerLoggedInEvent event){
        if(event.getPlayer() instanceof ServerPlayer player){
            PacketTarget target = PacketDistributor.PLAYER.with(() -> player);
            
            player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana ->
                NET_CHANNEL.send(target, new ManaSync(mana.getMana(), mana.getTotalManaCap(), mana.getAvailableManaCap())));
            
            player.getCapability(PlayerSelectedSpellsProvider.PRIMARY_SPELL).ifPresent(primary -> 
                NET_CHANNEL.send(target, new SelectedSpellsSync(true, primary.equippedSlot, primary.index)));
            
            player.getCapability(PlayerSelectedSpellsProvider.SECONDARY_SPELL).ifPresent(secondary ->
                NET_CHANNEL.send(target, new SelectedSpellsSync(false, secondary.equippedSlot, secondary.index)));

            player.getCapability(PlayerUtilityToggleProvider.UTILITY_TOGGLE).ifPresent(toggles -> 
                NET_CHANNEL.send(target, new UtilitySync(toggles.fire, toggles.earth, toggles.light, toggles.ender)));
        }
    }






}
