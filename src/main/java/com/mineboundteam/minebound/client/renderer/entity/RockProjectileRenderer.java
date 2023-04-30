package com.mineboundteam.minebound.client.renderer.entity;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.client.model.RockProjectileModel;
import com.mineboundteam.minebound.entity.RockProjectile;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RockProjectileRenderer extends EntityRenderer<RockProjectile> {
    public static final ResourceLocation DEFAULT_SKIN_LOCATION = new ResourceLocation(MineBound.MOD_ID, "textures/entity/rock_projectile.png");

    public RockProjectileRenderer(EntityRendererProvider.Context p_174289_, RockProjectileModel p_174290_, float p_174291_) {
        super(p_174289_);
    }
    public RockProjectileRenderer(EntityRendererProvider.Context c){
        this(c, new RockProjectileModel(), 0);
    }

    public ResourceLocation getTextureLocation(RockProjectile p_114482_) {
        return DEFAULT_SKIN_LOCATION;
    }

    @Override
    public boolean shouldShowName(RockProjectile c){
        return false;
    }
}
