package com.mineboundteam.minebound;


import com.mineboundteam.minebound.container.SpellHolderScreen;
import com.mineboundteam.minebound.registry.BlockRegistry;
import com.mineboundteam.minebound.registry.ContainerRegistry;
import com.mineboundteam.minebound.registry.Registry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(MineBound.MOD_ID)
public class MineBound {
    public static final String MOD_ID = "minebound";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final CreativeModeTab MINEBOUND_TAB = new CreativeModeTab(MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        @Override
        public @NotNull ItemStack makeIcon() { return new ItemStack(BlockRegistry.BOOM_BUTTON.get()); }
    };

    public MineBound(){
        IEventBus iEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.RegisterMod(iEventBus);
        iEventBus.addListener(this::clientSetup);
        iEventBus.addListener(this::enqueueIMC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent fmlClientSetupEvent) {
        MenuScreens.register(ContainerRegistry.SPELL_HOLDER_CONTAINER.get(), SpellHolderScreen::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Curios ring
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
    }
}