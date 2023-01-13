package com.mineboundteam.minebound.client.model;

import com.mineboundteam.minebound.entities.MyriCorpse;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class MyriCorpseModel extends EntityModel<MyriCorpse> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "myri_converted"), "main");
	private final ModelPart VoxelShapes;

	public MyriCorpseModel() {
		this.VoxelShapes = createBodyLayer().bakeRoot().getChild("VoxelShapes");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition VoxelShapes = partdefinition.addOrReplaceChild("VoxelShapes", CubeListBuilder.create().texOffs(-14, 65).addBox(-23.0F, 8.0F, -12.0F, 17.0F, 0.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0252F, 2.0609F, -15.0F, 19.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(24, 42).addBox(-15.3579F, 3.0609F, -6.8731F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(47, 45).addBox(-2.7163F, -1.457F, -14.4845F, 6.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(21, 50).addBox(-3.7725F, -0.457F, -9.4429F, 6.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-23.3583F, 0.8554F, -14.1718F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(53, 16).addBox(-17.695F, 2.8554F, -12.8753F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 54).addBox(-20.3583F, 1.8554F, -8.1718F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(52, 38).addBox(-21.4389F, -0.1657F, -14.9187F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 16.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		VoxelShapes.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 7).addBox(-2.0F, -1.0F, -3.5F, 4.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.7863F, 1.6334F, -15.9578F, 0.3927F, 0.0F, 0.0F));

		VoxelShapes.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(54, 24).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.6852F, 4.8554F, -6.3723F, 0.0F, -0.7854F, 0.0F));

		VoxelShapes.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(53, 28).addBox(-14.6547F, -10.9978F, 5.6317F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.4788F, 8.5408F, -13.2973F, -0.3927F, 0.0F, 0.0F));

		VoxelShapes.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(28, 34).addBox(-3.4693F, 0.0F, -5.6955F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.3579F, 3.0609F, -14.1269F, 0.0F, 0.3927F, 0.0F));

		VoxelShapes.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 14).addBox(-3.148F, 0.0F, -2.7284F, 4.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6158F, 2.6067F, -1.5552F, 0.0F, 0.3927F, 0.0F));

		VoxelShapes.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(19, 14).addBox(1.9567F, -2.5549F, -4.2853F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4822F, 5.543F, -11.0F, 0.0F, 0.3927F, 0.0F));

		VoxelShapes.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 46).addBox(-7.1511F, -5.1182F, -1.8276F, 9.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.7301F, 8.1226F, -17.7105F, 0.0F, 0.0F, -0.3927F));

		VoxelShapes.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(46, 0).addBox(-2.5749F, -4.7008F, -1.7868F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.7301F, 8.1226F, -17.7105F, 0.0F, 0.0F, 0.2618F));

		VoxelShapes.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(39, 55).addBox(4.0795F, -1.7108F, -2.2132F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.7301F, 4.1226F, -17.7105F, 0.0F, 0.0F, 0.3927F));

		VoxelShapes.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(25, 24).addBox(1.9567F, -2.5549F, -0.7147F, 12.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4822F, 5.543F, -11.0F, 0.0F, -0.3927F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(MyriCorpse entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		VoxelShapes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}