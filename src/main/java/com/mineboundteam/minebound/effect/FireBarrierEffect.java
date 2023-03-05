package com.mineboundteam.minebound.effect;

import com.mineboundteam.minebound.client.registry.ClientRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class FireBarrierEffect extends MobEffect {
    private static boolean fireBarrierActive = false;

    protected FireBarrierEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }

    @SubscribeEvent
    public static void toggleFireBarrier(InputEvent.KeyInputEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS && event.getKey() == ClientRegistry.FIRE_UTILITY_SPELL_TOGGLE.getKey().getValue())
            fireBarrierActive = !fireBarrierActive;
    }

}
