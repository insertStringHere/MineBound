package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.block.AlloyFurnace;
import com.mineboundteam.minebound.block.ArmorForge;
import com.mineboundteam.minebound.block.Magelight;
import com.mineboundteam.minebound.block.MultiDirectionalBlock;
import com.mineboundteam.minebound.block.entity.AlloyFurnaceBlockEntity;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
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
    public static final RegistryObject<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", () -> new AlloyFurnace(Properties.copy(Blocks.FURNACE)));
    public static final RegistryObject<Block> ARMOR_FORGE = BLOCKS.register("armor_forge", () -> new ArmorForge(Properties.copy(Blocks.ANVIL)));
    public static final RegistryObject<Block> ENERGIZED_IRON_BLOCK = BLOCKS.register("energized_iron_block", () -> new Block(Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> ILLUMINANT_STEEL_BLOCK = BLOCKS.register("illuminant_steel_block", () -> new Block(Properties.copy(Blocks.IRON_BLOCK).strength(7, 9).lightLevel(i -> 13)));
    public static final RegistryObject<Block> GILDED_DIAMOND_BLOCK = BLOCKS.register("gilded_diamond_block", () -> new Block(Properties.copy(Blocks.DIAMOND_BLOCK).strength(5, 9)));
    public static final RegistryObject<Block> MAGELIGHT = BLOCKS.register("magelight", () -> new Magelight(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.NONE).lightLevel((blockState) -> 15)));
    public static final RegistryObject<Block> MYRIAL_CONSOLE = BLOCKS.register("myrial_console", () -> new MultiDirectionalBlock(Properties.copy(Blocks.NETHERITE_BLOCK).lightLevel((i) -> 10)));
    public static final RegistryObject<Block> MYRIAL_GLASS = BLOCKS.register("myrial_glass", () -> new Block(Properties.copy(Blocks.GLASS).noOcclusion().requiresCorrectToolForDrops().strength(30f, 500f)));
    public static final RegistryObject<Block> MYRIAL_STEEL_BLOCK = BLOCKS.register("myrial_steel_block", () -> new Block(Properties.copy(Blocks.NETHERITE_BLOCK)));
    public static final RegistryObject<Block> MYRIAL_LOCKER = BLOCKS.register("myrial_locker", () -> new MultiDirectionalBlock(Properties.copy(Blocks.NETHERITE_BLOCK)));
    public static final RegistryObject<Block> MYRIAL_STEEL_PLATE = BLOCKS.register("myrial_steel_plate", () -> new Block(Properties.copy(Blocks.NETHERITE_BLOCK))); 
    public static final RegistryObject<Block> MYRIAL_DIAMOND_BLOCK = BLOCKS.register("myrial_diamond_block", () -> new Block(Properties.copy(Blocks.DIAMOND_BLOCK).strength(50f)));

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MineBound.MOD_ID);
    public static final RegistryObject<BlockEntityType<AlloyFurnaceBlockEntity>> ALLOY_FURNACE_ENTITY = BLOCK_ENTITIES.register("alloy_furnace", () -> BlockEntityType.Builder.of(AlloyFurnaceBlockEntity::new, BlockRegistry.ALLOY_FURNACE.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "alloy_furnace")));

    @SubscribeEvent
    public static void RegisterBlockItems(RegistryEvent.Register<Item> event){
        IForgeRegistry<Item> registry = event.getRegistry();
        for (RegistryObject<Block> b : BLOCKS.getEntries()){
            BlockItem blockItem = new BlockItem(b.get(), new Item.Properties().tab(MineBound.MINEBOUND_TAB));
            blockItem.setRegistryName(b.getId());
            if (!registry.containsKey(blockItem.getRegistryName()))
                registry.register(blockItem);
        }
    }

}
