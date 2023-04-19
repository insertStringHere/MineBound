package com.mineboundteam.minebound.particle;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.registry.MagicConfigRegistry;
import com.mineboundteam.minebound.particle.magic.FireOffensiveParticles;
import com.mineboundteam.minebound.particle.registry.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleEvents {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.FIRE_OFFENSIVE_1.get(),
                pSprites -> new FireOffensiveParticles.Provider(pSprites, MagicConfigRegistry.FIRE_OFFENSIVE_1));
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.FIRE_OFFENSIVE_2.get(),
                pSprites -> new FireOffensiveParticles.Provider(pSprites, MagicConfigRegistry.FIRE_OFFENSIVE_2));
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.FIRE_OFFENSIVE_3.get(),
                pSprites -> new FireOffensiveParticles.Provider(pSprites, MagicConfigRegistry.FIRE_OFFENSIVE_3));
    }
}