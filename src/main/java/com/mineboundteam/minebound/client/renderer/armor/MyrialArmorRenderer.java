package com.mineboundteam.minebound.client.renderer.armor;

import com.mineboundteam.minebound.client.model.MyrialArmorModel;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;

import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MyrialArmorRenderer extends GeoArmorRenderer<MyrialArmorItem> {

    public MyrialArmorRenderer() {
        super(new MyrialArmorModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }
}
