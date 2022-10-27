package com.mineboundteam.minebound.block;

import com.mineboundteam.minebound.MineBound;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
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
    public static RegistryObject<Block> BOOM_BUTTON = BLOCKS.register("boom_button", () -> new BoomButton(Block.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> DUSTY_BLOCK = BLOCKS.register("dusty_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(9f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> JELLO_CRYSTAL_BLOCK = BLOCKS.register("jello_crystal_block", () -> new GlassBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_RED).strength(1.0f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> SPELL_HOLDER = BLOCKS.register("spell_holder", () -> new SpellHolder(BlockBehaviour.Properties.of(Material.METAL).strength(99999999999f)));
    public static final RegistryObject<Block> WHEEL_OF_CHEESE = BLOCKS.register("wheel_of_cheese", () -> new Block(Block.Properties.copy(Blocks.CAKE)));

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event){
        IForgeRegistry<Item> iForgeRegistry = event.getRegistry();

        for (RegistryObject<net.minecraft.world.level.block.Block> registryObject : BLOCKS.getEntries()) {
            BlockItem blockItem = new BlockItem(registryObject.get(), new Item.Properties().tab(MineBound.MINEBOUND_TAB));
            blockItem.setRegistryName(registryObject.getId());
            iForgeRegistry.register(blockItem);
        }
    }
}
