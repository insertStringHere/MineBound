package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.client.renderer.entity.MyriCorpseRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EntityRendererRegistry {

    public static void register(){
        EntityRenderers.register(EntityRegistry.MYRI_CORPSE.get(), MyriCorpseRenderer::new);
    }

}

