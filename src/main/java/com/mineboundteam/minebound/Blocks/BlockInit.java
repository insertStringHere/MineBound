package com.mineboundteam.minebound.Blocks;

import com.mineboundteam.minebound.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "minebound");
    public static RegistryObject<Block> BOOM_BUTTON = BLOCKS.register("boom_button", () -> new BoomButton(Block.Properties.copy(Blocks.STONE_BUTTON)));

    public static final RegistryObject<Block> DUSTY_BLOCK = BLOCKS.register("dusty_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(9f).requiresCorrectToolForDrops()));

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event){
        final var registry = event.getRegistry();

        for (var block : BLOCKS.getEntries()) {
            final BlockItem b = new BlockItem(block.get(), new Item.Properties());
            b.setRegistryName(block.getId());
            registry.register(b);
        }
    }



    public static void register(IEventBus eventBus){
        BlockInit.BLOCKS.register(eventBus);
    }
}
