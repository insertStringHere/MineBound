package com.mineboundteam.minebound.item.armor;

import com.mineboundteam.minebound.config.ArmorConfig;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MyrialArmorItem extends GeoArmorItem implements IAnimatable {
    private final ArmorConfig config;
    private final ArmorTier tier;

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public MyrialArmorItem(ArmorMaterial pMaterial, EquipmentSlot pSlot, Properties pProperties, ArmorTier pTier, ArmorConfig pConfig) {
        super(pMaterial, pSlot, pProperties.durability(pConfig.ENERGY.get()));
        this.config = pConfig;
        this.tier = pTier;
    }

    public ArmorConfig getConfig(){
        return config;
    }
    
    public ArmorTier getTier(){
        return tier;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<MyrialArmorItem>(this, getDescriptionId(), 20, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event){
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
