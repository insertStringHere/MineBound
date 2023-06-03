package com.mineboundteam.minebound.client.renderer.entity;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.entity.MyrialSwordEntity;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MyrialSwordRenderer extends EntityRenderer<MyrialSwordEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MineBound.MOD_ID, "textures/item/myrial_sword.png");
    private final ItemRenderer itemRenderer;

    public MyrialSwordRenderer(Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    /**
     * Derived from {@link net.minecraft.client.renderer.entity.ItemEntityRenderer#render(ItemEntity, float, float, PoseStack, MultiBufferSource, int)}
     */
    @Override
    public void render(MyrialSwordEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        ItemStack itemstack = ItemRegistry.MYRIAL_SWORD.get().getDefaultInstance();
        BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, pEntity.level, null, pEntity.getId());
        // Increase size slightly
        pPoseStack.scale(1.15f, 1.15f, 1.15f);
        // Center model within hit box
        pPoseStack.translate(0.0D, 0.45F, 0.0D);
        // Rotate model so it always faces the player
        // Must do Y rotation then X rotation
        float yRot = pEntity.getViewYRot(pPartialTick);
        pPoseStack.mulPose(Vector3f.YN.rotationDegrees(yRot));
        float xRot = pEntity.getViewXRot(pPartialTick);
        pPoseStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
        // Rotate model so it continually spins
        float spinRotation = ((float) pEntity.tickCount + pPartialTick) / 1.75F;
        pPoseStack.mulPose(Vector3f.ZP.rotation(spinRotation));
        // Render
        this.itemRenderer.render(itemstack, ItemTransforms.TransformType.GROUND, false, pPoseStack, pBuffer,
                pPackedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(MyrialSwordEntity pEntity) {
        return TEXTURE;
    }
}
