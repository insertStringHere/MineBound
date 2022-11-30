package com.mineboundteam.minebound.registry;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.block.BoomButton;
import com.mineboundteam.minebound.block.AlloyFurnace;
import com.mineboundteam.minebound.block.CustomVoxelShape;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MineBound.MOD_ID);
    public static final RegistryObject<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", () -> new AlloyFurnace(BlockBehaviour.Properties.of(Material.HEAVY_METAL).strength(1.5f)));
    public static final RegistryObject<Block> BOOM_BUTTON = BLOCKS.register("boom_button", () -> new BoomButton(Block.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> CUSTOM_VOXEL_SHAPE = BLOCKS.register("custom_voxel_shape", () -> new CustomVoxelShape(BlockBehaviour.Properties.of(Material.HEAVY_METAL).strength(1.5f)));

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event){
        IForgeRegistry<Item> iForgeRegistry = event.getRegistry();

        for (RegistryObject<Block> registryObject : BLOCKS.getEntries()) {
            BlockItem blockItem = new BlockItem(registryObject.get(), new Item.Properties().tab(MineBound.MINEBOUND_TAB));
            blockItem.setRegistryName(registryObject.getId());
            iForgeRegistry.register(blockItem);
        }
    }
}
