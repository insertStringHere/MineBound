package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.block.MultiDirectionalBlock;
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

    public static final RegistryObject<Block> ENERGIZED_IRON_BLOCK = BLOCKS.register("energized_iron_block", () -> new Block(Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> GILDED_DIAMOND_BLOCK = BLOCKS.register("gilded_diamond_block", () -> new Block(Properties.copy(Blocks.DIAMOND_BLOCK).strength(9)));
    public static final RegistryObject<Block> MYRIAL_CONSOLE = BLOCKS.register("myrial_console", () -> new MultiDirectionalBlock(Properties.copy(Blocks.NETHERITE_BLOCK).lightLevel((i) -> 8)));
    public static final RegistryObject<Block> MYRIAL_GLASS = BLOCKS.register("myrial_glass", () -> new Block(Properties.copy(Blocks.GLASS).noOcclusion().requiresCorrectToolForDrops().strength(30f, 500f)));
    public static final RegistryObject<Block> MYRIAL_STEEL_BLOCK = BLOCKS.register("myrial_steel_block", () -> new Block(Properties.copy(Blocks.NETHERITE_BLOCK)));
    public static final RegistryObject<Block> MYRIAL_LOCKER = BLOCKS.register("myrial_locker", () -> new MultiDirectionalBlock(Properties.copy(Blocks.NETHERITE_BLOCK)));
    public static final RegistryObject<Block> MYRIAL_STEEL_PLATE = BLOCKS.register("myrial_steel_plate", () -> new Block(Properties.copy(Blocks.NETHERITE_BLOCK))); 

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
