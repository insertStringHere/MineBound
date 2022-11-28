package com.mineboundteam.minebound.event;

import org.lwjgl.glfw.GLFW;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.registry.KeyRegistry;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class GlowingEvent {
    static {
        GlowingEvent.mc = Minecraft.getInstance();
    }

    private static boolean glowActive = false;
    private static Minecraft mc;
    private static OutlineBufferSource outlineBufferSource; 

    private static boolean isRendering = false; 

    // https://github.com/TeamLapen/Vampirism/blob/1.18/src/main/java/de/teamlapen/vampirism/client/render/RenderHandler.java
    @SubscribeEvent
    public static void displayGlowing(RenderLivingEvent.Post<?,?> e) {
        if(!isRendering && glowActive){
            if(outlineBufferSource == null){
                outlineBufferSource = new OutlineBufferSource(GlowingEvent.mc.renderBuffers().bufferSource());
                outlineBufferSource.setColor(0xee, 0xee, 0xee, 255);
            }

            LivingEntity entity = e.getEntity();
            if (!entity.equals(mc.player) && glowActive && entity.distanceTo(mc.player) < 200) {
                EntityRenderDispatcher erd = mc.getEntityRenderDispatcher();    
                float f = Mth.lerp(e.getPartialTick(), entity.yRotO, entity.getYRot());
                isRendering = true;
                erd.getRenderer(entity).render(entity, f, e.getPartialTick(), e.getPoseStack(), outlineBufferSource, erd.getPackedLightCoords(entity, e.getPartialTick()));
                isRendering = false;
            }
        }
    }

    @SubscribeEvent
    public static void toggleGlow(KeyInputEvent e) {
        if (e.getAction() == GLFW.GLFW_PRESS && e.getKey() == KeyRegistry.TOGGLE_GLOW.getKey().getValue())
            glowActive = !glowActive;
    }
}
