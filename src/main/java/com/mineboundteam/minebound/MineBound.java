package com.mineboundteam.minebound;

import com.mineboundteam.minebound.capabilities.network.CapabilitySync;
import com.mineboundteam.minebound.client.registry.ClientRegistry;
import com.mineboundteam.minebound.entity.registry.EntityRegistry;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import com.mineboundteam.minebound.magic.network.MagicSync;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(MineBound.MOD_ID)
public class MineBound {
    public static final String MOD_ID = "minebound";
    public static final CreativeModeTab MINEBOUND_TAB = new CreativeModeTab(MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.MYRI_MANASAC.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> list){
            super.fillItemList(list);
            list.sort((curr, next) -> {
                if(curr.getItem() instanceof BlockItem && next.getItem() instanceof BlockItem)
                    return 0;
                if(curr.getItem() instanceof BlockItem)
                    return -1;
                if(next.getItem() instanceof BlockItem)
                    return 1;
                return 0;
            });
        }
    };
    public static final CreativeModeTab SPELLS_TAB = new CreativeModeTab(MOD_ID + "_spells") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.TELEKINETIC_OFFENSIVE_1.get());
        }
    };

    public static final int MANA_COLOR = (77 << 16) + (106 << 8) + (255);

    public MineBound(){
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.RegisterMod(iEventBus);
        iEventBus.addListener(this::clientSetup);
        iEventBus.addListener(EntityRegistry::registerAttributes);
        iEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent fmlClientSetupEvent) {
        ClientRegistry.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event){
        CapabilitySync.registerPackets(event);
        MagicSync.registerPackets(event);
    }
}
