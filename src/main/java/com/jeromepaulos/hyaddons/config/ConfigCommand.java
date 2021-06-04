package com.jeromepaulos.hyaddons.config;

import com.jeromepaulos.hyaddons.HyAddons;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class ConfigCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "hyaddons";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>() {{
            add("hy");
        }};
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        HyAddons.guiToOpen = HyAddons.config.gui();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}
