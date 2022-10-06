package com.mason.minebound;

import com.mason.minebound.init.BlockInit;
import com.mason.minebound.init.ItemInit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("minebound")
public class MineBound {
    public static final String MOD_ID = "minebound";
    public static final CreativeModeTab MINEBOUND_TAB = new CreativeModeTab(MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack makeIcon() { return new ItemStack(ItemInit.JELLO_ITEM.get()); }
    };

    public MineBound() {
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEMS.register(iEventBus);
        BlockInit.BLOCKS.register(iEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
