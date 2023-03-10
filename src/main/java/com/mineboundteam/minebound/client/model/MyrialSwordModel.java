package com.mineboundteam.minebound.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

public class MyrialSwordModel extends Model {
	private final ModelPart modelPart;

	public MyrialSwordModel(ModelPart modelPart) {
		super(RenderType::entitySolid);
		this.modelPart = modelPart;
	}

	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.modelPart.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
