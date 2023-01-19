package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.block.AlloyFurnace;
import com.mineboundteam.minebound.block.MyrialConsoleBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
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
    public static final RegistryObject<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", () -> new AlloyFurnace(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F)));
    public static final RegistryObject<Block> MYRIAL_CONSOLE = BLOCKS.register("myrial_console", () -> new MyrialConsoleBlock(Properties.copy(Blocks.NETHERITE_BLOCK).lightLevel((i) -> 8)));
    public static final RegistryObject<Block> MYRIAL_GLASS = BLOCKS.register("myrial_glass", () -> new Block(Properties.copy(Blocks.GLASS).noOcclusion().requiresCorrectToolForDrops().strength(30f, 500f)));
    public static final RegistryObject<Block> MYRIAL_STEEL_BLOCK = BLOCKS.register("myrial_steel_block", () -> new Block(Properties.copy(Blocks.NETHERITE_BLOCK)));

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
