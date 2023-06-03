package com.mineboundteam.minebound.client.renderer.entity;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.client.model.RockProjectileModel;
import com.mineboundteam.minebound.entity.RockProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RockProjectileRenderer extends EntityRenderer<RockProjectile> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MineBound.MOD_ID, "textures/entity/rock_projectile.png");
    private final RockProjectileModel model;

    public RockProjectileRenderer(EntityRendererProvider.Context c) {
        super(c);
        this.model = new RockProjectileModel(RockProjectileModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void render(RockProjectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RockProjectile pEntity) {
        return TEXTURE;
    }

}

