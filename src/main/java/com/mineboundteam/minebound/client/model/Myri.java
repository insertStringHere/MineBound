package com.mineboundteam.minebound.client.model;

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
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Myri<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "myri"), "main");
	private final ModelPart Chest;

	public Myri(ModelPart root) {
		this.Chest = root.getChild("Chest");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Chest = partdefinition.addOrReplaceChild("Chest", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -40.0F, -6.0F, 6.0F, 19.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(80, -8).addBox(-2.0F, -40.0F, -6.0F, 0.0F, 18.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 23.0F, 2.0F));

		PartDefinition thighR = Chest.addOrReplaceChild("thighR", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, 2.0F));

		thighR.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-3.0F, -7.0F, -3.0F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.2637F, -15.2386F, 0.262F, 0.3417F, 0.4985F, 0.3407F));

		PartDefinition shinR = thighR.addOrReplaceChild("shinR", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -6.0F));

		shinR.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(50, 27).addBox(-1.0F, -4.0F, -1.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.0F, 6.0F, -0.4037F, 0.3834F, -0.8615F));

		PartDefinition footR = shinR.addOrReplaceChild("footR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		footR.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 61).addBox(-1.0F, -6.0F, -2.0F, 2.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.1733F, 0.3542F, 0.4674F));

		footR.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(60, 15).addBox(-6.0F, 0.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 6.0F, -0.0317F, 0.3477F, -0.0928F));

		PartDefinition thighL = Chest.addOrReplaceChild("thighL", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, -6.0F));

		thighL.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 27).addBox(-3.0F, -7.0F, -2.0F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.2637F, -15.2386F, -0.262F, -0.3417F, -0.4985F, 0.3407F));

		PartDefinition shinL = thighL.addOrReplaceChild("shinL", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, 6.0F));

		shinL.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, -4.0F, -3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -8.0F, -6.0F, 0.4037F, -0.3834F, -0.8615F));

		PartDefinition footL = shinL.addOrReplaceChild("footL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		footL.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(60, 60).addBox(-1.0F, -6.0F, -2.0F, 2.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -6.0F, -0.1733F, -0.3542F, 0.4674F));

		footL.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(60, 9).addBox(-6.0F, 0.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, -6.0F, 0.0317F, -0.3477F, -0.0928F));

		PartDefinition armR = Chest.addOrReplaceChild("armR", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -26.0F, 5.0F, 0.0F, 0.0F, 0.1745F));

		armR.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(32, 50).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, -1.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition forearmR = armR.addOrReplaceChild("forearmR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		forearmR.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(16, 46).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -0.2182F, -0.1309F, 0.3054F));

		PartDefinition armL = Chest.addOrReplaceChild("armL", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -26.0F, -9.0F, 0.0F, 0.0F, 0.1745F));

		armL.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(44, 13).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 1.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition forearmL = armL.addOrReplaceChild("forearmL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		forearmL.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.2182F, 0.1309F, 0.3054F));

		PartDefinition Tail1 = Chest.addOrReplaceChild("Tail1", CubeListBuilder.create(), PartPose.offset(-1.0F, -26.0F, -9.0F));

		Tail1.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(20, 34).addBox(-3.5F, -3.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 6.0F, 7.0F, 0.0F, 0.0F, 0.48F));

		PartDefinition Tail2 = Tail1.addOrReplaceChild("Tail2", CubeListBuilder.create(), PartPose.offset(6.5F, 6.0F, 7.0F));

		Tail2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(48, 50).addBox(1.5F, -3.0F, -2.0F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition Tail3 = Tail2.addOrReplaceChild("Tail3", CubeListBuilder.create(), PartPose.offset(16.382F, 11.6784F, 0.0F));

		Tail3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(58, 40).addBox(6.5F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.382F, -11.6784F, 0.0F, 0.0F, 0.0F, 0.7418F));

		PartDefinition Tail4 = Tail3.addOrReplaceChild("Tail4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		Tail4.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(60, 21).addBox(-3.0F, -3.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9542F, -1.2068F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition Tail5 = Tail4.addOrReplaceChild("Tail5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		Tail5.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(36, 34).addBox(-2.0F, -2.0F, -1.0F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition Tail6 = Tail5.addOrReplaceChild("Tail6", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		Tail6.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(62, 27).addBox(3.0F, -1.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition Head = Chest.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(20, 19).addBox(-2.0F, -48.0F, -6.0F, 7.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(44, 60).addBox(-1.0F, -44.0F, -4.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(36, 40).addBox(-6.0F, -45.0F, -5.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		Head.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -46.0F, -6.0F, 0.3927F, 0.0F, 0.7854F));

		Head.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(64, 0).addBox(-2.5F, -3.5F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.9466F, -49.2395F, -8.1048F, 0.2533F, 0.067F, 0.5321F));

		Head.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(28, 64).addBox(-2.5F, -3.5F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.9466F, -49.2395F, 4.1048F, -0.2533F, -0.067F, 0.5321F));

		Head.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(62, 48).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -46.0F, 2.0F, -0.3927F, 0.0F, 0.7854F));

		Head.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 59).addBox(-2.5F, 0.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -45.0F, -2.0F, 0.0F, 0.0F, -1.0036F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Chest.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}