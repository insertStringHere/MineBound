package com.mineboundteam.minebound.item.armor;

import java.util.function.Consumer;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.config.ArmorConfig;
import com.mineboundteam.minebound.config.ManaConfig;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

@Mod.EventBusSubscriber(modid = MineBound.MOD_ID)
public class MyrialArmorItem extends GeoArmorItem implements IAnimatable {
    private final ArmorConfig config;
    private final ArmorTier tier;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public MyrialArmorItem(ArmorMaterial pMaterial, EquipmentSlot pSlot, Properties pProperties, ArmorTier pTier, ArmorConfig pConfig) {
        super(pMaterial, pSlot, pProperties.durability(pConfig.ENERGY.get()));
        this.config = pConfig;
        this.tier = pTier;        
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken){
        // Remove normal damage, we're not using durability for durability
        return 0;
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

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(ManaConfig.keepArmor.get() && event.isWasDeath())
            for(ItemStack item : event.getOriginal().getArmorSlots())
                if(!item.isEmpty() && item.getItem() instanceof MyrialArmorItem)
                    event.getPlayer().setItemSlot(item.getEquipmentSlot(), item);
    }
}
