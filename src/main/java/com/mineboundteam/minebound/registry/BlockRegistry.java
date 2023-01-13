package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.block.MyrialConsoleBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MineBound.MOD_ID);
    
    public static final RegistryObject<Block> MYRIAL_CONSOLE = BLOCKS.register("myrial_console", () -> new MyrialConsoleBlock(Properties.copy(Blocks.NETHERITE_BLOCK).lightLevel((i) -> 8)));

    @SubscribeEvent
    public static void RegisterBlockItems(RegistryEvent.Register<Item> event){
        IForgeRegistry<Item> registry = event.getRegistry();
        for(RegistryObject<Block> b : BLOCKS.getEntries()){
            BlockItem blockItem = new BlockItem(b.get(), new Item.Properties().tab(MineBound.MINEBOUND_TAB));
            blockItem.setRegistryName(b.getId());
            if(!registry.containsKey(blockItem.getRegistryName()))
                registry.register(blockItem);
        }
    }

}   
