package com.mineboundteam.minebound.client.renderer.entity;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.client.model.MyriCorpseModel;
import com.mineboundteam.minebound.entity.MyriCorpse;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class MyriCorpseRenderer extends LivingEntityRenderer<MyriCorpse, MyriCorpseModel>{
    public static final ResourceLocation DEFAULT_SKIN_LOCATION = new ResourceLocation(MineBound.MOD_ID, "textures/entity/myri_corpse.png");

    public MyriCorpseRenderer(Context p_174289_, MyriCorpseModel p_174290_, float p_174291_) {
        super(p_174289_, p_174290_, p_174291_);
    }
    public MyriCorpseRenderer(Context c){
        this(c, new MyriCorpseModel(), 0);
    }

    @Override
    public ResourceLocation getTextureLocation(MyriCorpse p_114482_) {
        return DEFAULT_SKIN_LOCATION;
    }

    @Override
    public boolean shouldShowName(MyriCorpse c){
        return false;
    }


    
    
}
