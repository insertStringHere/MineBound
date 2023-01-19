package com.mineboundteam.minebound.client.model;

import java.util.HashMap;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.item.armor.ArmorTier;
import com.mineboundteam.minebound.item.armor.MyrialArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MyrialArmorModel extends AnimatedGeoModel<MyrialArmorItem> {
    private static final ResourceLocation modelResource = new ResourceLocation(MineBound.MOD_ID, "geo/myrial_armor.geo.json");
	private static final ResourceLocation animationResource = new ResourceLocation(MineBound.MOD_ID, "animations/myrial_armor.animation.json");

    private static final HashMap<ArmorTier, ResourceLocation> textures = new HashMap<>();

    static{
        textures.put(ArmorTier.EFFIGY, new ResourceLocation(MineBound.MOD_ID, "textures/models/armor/myrial_effigy.png"));
        textures.put(ArmorTier.SUIT, new ResourceLocation(MineBound.MOD_ID, "item/models/myrial_suit.png"));
        textures.put(ArmorTier.SYNERGY, new ResourceLocation(MineBound.MOD_ID, "item/models/myrial_synergy.png"));
        textures.put(ArmorTier.SINGULARITY, new ResourceLocation(MineBound.MOD_ID, "item/models/myrial_singularity.png"));
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MyrialArmorItem animatable) {
        return animationResource;
    }

    @Override
    public ResourceLocation getModelLocation(MyrialArmorItem object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureLocation(MyrialArmorItem object) {
        if(textures.containsKey(object.getTier()))
            return textures.get(object.getTier());
        
        textures.put(object.getTier(), new ResourceLocation(object.getRegistryName().getNamespace(), "models/armor" + object.getRegistryName().getPath() + ".png"));
        return textures.get(object.getTier());
    }
    
}
