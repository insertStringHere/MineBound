package com.mineboundteam.minebound.worldgen;

import java.util.Random;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier.Context;

public class MyrialBase extends JigsawFeature {
    public static final Random rand = new Random();
    
    public MyrialBase(Codec<JigsawConfiguration> pCodec, Predicate<Context<JigsawConfiguration>> pPredicate) {
        super(pCodec, rand.nextInt(-45, 40), true, false, pPredicate);
    }
}
