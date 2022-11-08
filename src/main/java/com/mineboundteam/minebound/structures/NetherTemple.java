package com.mineboundteam.minebound.structures;

import com.mineboundteam.minebound.MineBound;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.VillageFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;


// Minecraft by default does all the work, but if we need more specific stuff, we
// can customize this class further by making it extend StructureFeature instead.
public class NetherTemple extends StructureFeature<JigsawConfiguration> {  
    public NetherTemple() {
        super(JigsawConfiguration.CODEC, NetherTemple::createPiecesGenerator, new PostPlacementProcessor() {
            public void afterPlace(WorldGenLevel p_192438_, StructureFeatureManager p_192439_, ChunkGenerator p_192440_, Random p_192441_, BoundingBox p_192442_, ChunkPos p_192443_, PiecesContainer p_192444_){
                // This is where extra stuff can happen
            }
        });
    }
    
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    // Taken from https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.18.2-Forge-Jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/structures/SkyStructures.java
    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {

        // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);

        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator = JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos, false,  true);

        if(structurePiecesGenerator.isPresent()) {
            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
            MineBound.LOGGER.log(Level.DEBUG, "Nether Temple at {}", blockpos);
        }
        return structurePiecesGenerator;
    }

    
}



