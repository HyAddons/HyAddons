package com.jeromepaulos.hyaddons.config;

import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.features.ColoredNames;
import com.jeromepaulos.hyaddons.utils.SummonUtils;
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
        if(args.length > 0) {
            switch (args[0]) {
                case "refreshNames":
                    ColoredNames.loadNames();
                break;
                case "refreshSummons":
                    SummonUtils.loadSkins();
                break;
                case "logSummons":
                    System.out.println(SummonUtils.skins);
                break;
                case "logNames":
                    System.out.println(ColoredNames.users);
                break;
            }
        } else {
            HyAddons.guiToOpen = HyAddons.config.gui();
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}
