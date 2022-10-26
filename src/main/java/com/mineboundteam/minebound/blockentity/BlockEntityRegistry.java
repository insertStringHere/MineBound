package com.mineboundteam.minebound.blockentity;

import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.block.BlockInit;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MineBound.MOD_ID);
    public static final RegistryObject<BlockEntityType<SpellHolderBlockEntity>> SPELL_HOLDER_BLOCK_ENTITY = BLOCK_ENTITIES.register("spell_holder_block_entity", () -> BlockEntityType.Builder.of(SpellHolderBlockEntity::new, BlockInit.SPELL_HOLDER.get()).build(null));
}
