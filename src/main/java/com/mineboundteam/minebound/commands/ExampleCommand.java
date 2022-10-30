package com.mineboundteam.minebound.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ExampleCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("examplecommand").executes(ExampleCommand::execute));
    }
    private static int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        Entity entity = command.getSource().getEntity();
        if (entity instanceof Player){
            Player player  = (Player)entity;
            BlockPos blockPos = player.blockPosition();
            String message = "Your Position is: X: " + blockPos.getX() + " Y: " + blockPos.getY() + " Z: " + blockPos.getZ();
            player.sendMessage(new TextComponent(message), Util.NIL_UUID);

        }


        return Command.SINGLE_SUCCESS;
    }
}
