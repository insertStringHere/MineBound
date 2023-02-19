package com.mineboundteam.minebound.particle;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.particle.magic.FireOffensiveParticles;
import com.mineboundteam.minebound.registry.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.FIRE_OFFENSIVE_1.get(),
                pSprites -> new FireOffensiveParticles.Provider(pSprites, 30));
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.FIRE_OFFENSIVE_2.get(),
                pSprites -> new FireOffensiveParticles.Provider(pSprites, 60));
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.FIRE_OFFENSIVE_3.get(),
                pSprites -> new FireOffensiveParticles.Provider(pSprites, 60));
    }
}
