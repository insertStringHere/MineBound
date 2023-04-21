package com.mineboundteam.minebound.client.renderer.entity;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.entity.FireProjectile;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FireProjectileRenderer extends EntityRenderer<FireProjectile> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MineBound.MOD_ID, "textures/entity/fire_projectile.png");

    public FireProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(FireProjectile pEntity) {
        return TEXTURE;
    }
}
