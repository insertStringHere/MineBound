package com.mineboundteam.minebound.client.renderer.entity;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MyrialSwordEntityRenderer extends EntityRenderer<MyrialSwordEntity> {
    public MyrialSwordEntityRenderer(Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MyrialSwordEntity myrialSwordEntity) {
        return new ResourceLocation(MineBound.MOD_ID, "textures/entity/myrial_sword_entity.png");
    }
}
