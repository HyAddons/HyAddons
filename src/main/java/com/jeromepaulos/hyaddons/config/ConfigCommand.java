package com.jeromepaulos.hyaddons.config;

import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.features.misc.ColoredNames;
import com.jeromepaulos.hyaddons.gui.MoveWidgetGui;
import com.jeromepaulos.hyaddons.utils.SummonUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

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
                    Utils.sendModMessage("Colored names refreshed");
                break;
                case "refreshSummons":
                    SummonUtils.loadSkins();
                    Utils.sendModMessage("Summon skins refreshed");
                break;
                case "gui":
                    HyAddons.guiToOpen = new MoveWidgetGui();
                break;
            }
        } else {
            HyAddons.guiToOpen = HyAddons.config.gui();
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 2) return CommandBase.getListOfStringsMatchingLastWord(args, "refreshNames", "refreshSummons", "gui");
        return null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}
