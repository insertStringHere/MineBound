package com.mineboundteam.minebound.worldgen;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;

import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier.Context;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

public class MyrialBase extends StructureFeature<JigsawConfiguration> {
    public static final Random rand = new Random();

    protected static final Field heightMap;

    static {
        Field temp = null;
        var fields = Context.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(LevelHeightAccessor.class)) {
                temp = field;
                break;
            }
        }

        if (temp == null) {
            Minecraft.crash(new CrashReport("Couldn't find HeightAccessor", null));
        }

        heightMap = temp;
        heightMap.setAccessible(true);
    }

    public MyrialBase(Codec<JigsawConfiguration> pCodec, Predicate<Context<JigsawConfiguration>> pPredicate) {
        super(pCodec, (context) -> {  
            Object accessorField = null;
            try {
                accessorField = heightMap.get(context);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Minecraft.crash(new CrashReport("Couldn't get HeightAccessor", e));
            }

            if (pPredicate.test(context) && accessorField != null && accessorField instanceof LevelHeightAccessor heightAccessor) {
                int minY = heightAccessor.getMinBuildHeight();
                int maxY = (int)((context.getLowestY(0, 15) - minY) * (4.0 / 5.0)) + minY;
                if(minY + 5 < maxY){
                    BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), rand.nextInt(minY + 5, maxY), context.chunkPos().getMinBlockZ());
                    Pools.bootstrap();
                    return JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos, true, false);
                }
            }
            return Optional.empty();
         });
    }
}
