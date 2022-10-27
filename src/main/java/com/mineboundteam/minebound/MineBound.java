package com.mineboundteam.minebound;

import com.mineboundteam.minebound.block.BlockRegistry;
import com.mineboundteam.minebound.blockentity.BlockEntityRegistry;
import com.mineboundteam.minebound.container.ContainerRegistry;
import com.mineboundteam.minebound.container.SpellHolderScreen;
import com.mineboundteam.minebound.effect.EffectRegistry;
import com.mineboundteam.minebound.item.ItemRegistry;
import com.mineboundteam.minebound.sound.SoundRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
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
import org.jetbrains.annotations.NotNull;

@Mod(MineBound.MOD_ID)
public class MineBound {
    public static final String MOD_ID = "minebound";
    public static final CreativeModeTab MINEBOUND_TAB = new CreativeModeTab(MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        @Override
        public @NotNull ItemStack makeIcon() { return new ItemStack(ItemRegistry.JELLO.get()); }
    };

    public MineBound(){
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        BlockEntityRegistry.BLOCK_ENTITIES.register(iEventBus);
        BlockRegistry.BLOCKS.register(iEventBus);
        ContainerRegistry.CONTAINERS.register(iEventBus);
        EffectRegistry.MOB_EFFECTS.register(iEventBus);
        ItemRegistry.ITEMS.register(iEventBus);
        SoundRegistry.SOUND_EVENTS.register(iEventBus);

        iEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent fmlClientSetupEvent) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.JELLO_CRYSTAL_BLOCK.get(), RenderType.translucent());
        MenuScreens.register(ContainerRegistry.SPELL_HOLDER_CONTAINER.get(), SpellHolderScreen::new);
    }
}
