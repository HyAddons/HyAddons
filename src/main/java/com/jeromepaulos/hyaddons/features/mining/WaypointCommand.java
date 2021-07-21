package com.jeromepaulos.hyaddons.features.mining;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.List;

public class WaypointCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "hypoint";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/"+getCommandName()+" <name> <x> <y> <z>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length > 0 && args.length <= 3) return func_175771_a(args, 0, pos);
        return null;
    }

}
