package com.mineboundteam.minebound.sound;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MineBound.MOD_ID);
    public static final RegistryObject<SoundEvent> FLARP_PLAYED_WITH = registerSoundEvent("flarp_played_with");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(MineBound.MOD_ID, name)));
    }
}
