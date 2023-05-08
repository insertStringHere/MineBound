package com.mineboundteam.minebound.client.renderer.entity;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.entity.RockProjectile;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RockProjectileRenderer extends EntityRenderer<RockProjectile> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(MineBound.MOD_ID, "textures/entity/rock_projectile.png");

    public RockProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(RockProjectile pEntity) {
      return TEXTURE;
    }

    }

