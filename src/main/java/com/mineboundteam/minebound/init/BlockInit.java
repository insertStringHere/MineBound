package com.mineboundteam.minebound.init;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.mineboundteam.minebound.MineBound;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MineBound.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = ItemInit.ITEMS;
    public static final RegistryObject<Block> JELLO_CRYSTAL_BLOCK = register("jello_crystal_block", () -> new GlassBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_RED).strength(1.0f).sound(SoundType.METAL)), object -> () -> new BlockItem(object.get(), new Item.Properties().tab(MineBound.MINEBOUND_TAB)));

    private static <T extends Block> RegistryObject<T> register(final String name, final Supplier<? extends T> block, Function<RegistryObject<T>, Supplier<? extends Item>> item) {
        RegistryObject<T> registryObject = registerBlock(name, block);
        ITEMS.register(name, item.apply(registryObject));
        return registryObject;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(final String name, final Supplier<? extends T> block) {
        return BLOCKS.register(name, block);
    }
}
