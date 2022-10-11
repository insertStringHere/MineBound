package com.mineboundteam.minebound;

import com.mineboundteam.minebound.block.BlockInit;
import com.mineboundteam.minebound.effect.EffectInit;
import com.mineboundteam.minebound.item.ItemInit;
import com.mineboundteam.minebound.sound.SoundInit;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MineBound.MOD_ID)
public class MineBound {
    public static final String MOD_ID = "minebound";
    public static final CreativeModeTab MINEBOUND_TAB = new CreativeModeTab(MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack makeIcon() { return new ItemStack(ItemInit.JELLO.get()); }
    };

    public MineBound(){
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        BlockInit.BLOCKS.register(iEventBus);
        EffectInit.MOB_EFFECTS.register(iEventBus);
        ItemInit.ITEMS.register(iEventBus);
        SoundInit.SOUND_EVENTS.register(iEventBus);

        iEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent fmlClientSetupEvent) {
        ItemBlockRenderTypes.setRenderLayer(BlockInit.JELLO_CRYSTAL_BLOCK.get(), RenderType.translucent());
    }
}
