package com.mineboundteam.minebound.magic.UtilitySpells;

import com.mineboundteam.minebound.MineBound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class LightUtilitySpell {
    public static boolean active = false;

    protected static PostChain entityEffect;

    private static boolean isRendering = false;
    private static boolean hasGlowing = false;
    private static Minecraft minecraft = Minecraft.getInstance();

    public static void initOutline() {
        Minecraft minecraft = Minecraft.getInstance();
        if (entityEffect == null) {
            try{
                var piss = LevelRenderer.class.getDeclaredField("entityEffect");
                piss.setAccessible(true);

                entityEffect = (PostChain)piss.get(minecraft.levelRenderer);
            } catch(Exception e){
                //i dont' care
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public static <T extends LivingEntity, M extends EntityModel<T>> void renderEntity(
            RenderLivingEvent.Pre<T, M> event) {
        if (!isRendering && active) {
            LocalPlayer player = minecraft.player;
            T entity = (T) event.getEntity();
            LivingEntityRenderer<T, M> renderer = event.getRenderer();

            if (!entity.equals(player) && entity.distanceTo(player) < 200) {
                isRendering = true;
                if(entity.isCurrentlyGlowing()){
                    hasGlowing = true;
                }

                OutlineBufferSource buff = minecraft.renderBuffers().outlineBufferSource();
                buff.setColor(0xee, 0xee, 0xee, 255);

                renderer.render(entity, 0, event.getPartialTick(), event.getPoseStack(), buff, event.getPackedLight());
                isRendering = false;
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("removal")
    public static void renderOutline(RenderLevelLastEvent event){
        if(!hasGlowing) {
            initOutline();
            entityEffect.process(event.getPartialTick());    
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }
        hasGlowing = false;
    }

}