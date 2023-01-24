package com.mineboundteam.minebound.registry.client;

import com.mineboundteam.minebound.client.renderer.armor.MyrialArmorRenderer;
import com.mineboundteam.minebound.client.renderer.entity.MyriCorpseRenderer;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;
import com.mineboundteam.minebound.registry.EntityRegistry;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@OnlyIn(Dist.CLIENT)
public class RendererRegistry {

    public static void register(){
        EntityRenderers.register(EntityRegistry.MYRI_CORPSE.get(), MyriCorpseRenderer::new);

        GeoArmorRenderer.registerArmorRenderer(MyrialArmorItem.class, () -> new MyrialArmorRenderer());
    }
}

