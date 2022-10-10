package com.mineboundteam.minebound.Blocks;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;


public class BoomButton extends Block {
    public BoomButton(Properties p) {
        super(p);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos position, Player player, InteractionHand hand, BlockHitResult hitResult){
        if(world.isClientSide())
            player.sendMessage(new TextComponent("Explosion imminent; beginning countdown"), UUID.randomUUID());
        new Thread(() -> {
            try{
                for(int i = 5; i > 0; i--){
                    Thread.sleep(1000);
                    if(world.isClientSide)
                        player.sendMessage(new TextComponent(String.format("%d", i)), UUID.randomUUID());
                }
                
                if(!world.isClientSide()){
                    world.explode(player, player.getX(), player.getY(), player.getZ(), 0f, true, Explosion.BlockInteraction.NONE);
                    player.kill();
                }

            }catch(InterruptedException e){
                System.err.println("Could not complete BoomButton detonation.");
                System.err.println(e);
            }
        }).run();
        return InteractionResult.SUCCESS;
    }
}
