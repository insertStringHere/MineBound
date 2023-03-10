package com.mineboundteam.minebound.client.renderer.entity;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.client.model.MyrialSwordModel;
import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class MyrialSwordRenderer extends EntityRenderer<MyrialSwordEntity> {
    private final MyrialSwordModel myrialSwordModel;

    public MyrialSwordRenderer(Context context) {
        super(context);
        // TODO complete myrial sword model ticket
        this.myrialSwordModel = new MyrialSwordModel(context.bakeLayer(ModelLayers.TRIDENT));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull MyrialSwordEntity myrialSwordEntity) {
        return new ResourceLocation(MineBound.MOD_ID, "textures/entity/myrial_sword_entity.png");
    }

    public void render(MyrialSwordEntity myrialSwordEntity, float entityYaw, float partialTicks, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, myrialSwordEntity.yRotO, myrialSwordEntity.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, myrialSwordEntity.xRotO, myrialSwordEntity.getXRot()) + 90.0F));
        this.myrialSwordModel.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect(multiBufferSource, this.myrialSwordModel.renderType(this.getTextureLocation(myrialSwordEntity)), false, false), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();
        super.render(myrialSwordEntity, entityYaw, partialTicks, poseStack, multiBufferSource, packedLight);
    }
}
