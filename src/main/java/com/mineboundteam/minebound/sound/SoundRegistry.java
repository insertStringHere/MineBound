package com.mineboundteam.minebound.sound;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MineBound.MOD_ID);
    public static final RegistryObject<SoundEvent> FLARP_PLAYED_WITH = SOUND_EVENTS.register("flarp_played_with", () -> new SoundEvent(new ResourceLocation(MineBound.MOD_ID, "flarp_played_with")));
}
