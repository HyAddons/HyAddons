package com.jeromepaulos.hyaddons.config;

import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.utils.SummonUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import com.jeromepaulos.hyaddons.features.misc.ColoredNames;
import com.jeromepaulos.hyaddons.gui.MoveWidgetGui;
import com.jeromepaulos.hyaddons.utils.DungeonUtils;
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
            switch(args[0]) {
                case "refresh":
                    if(args.length > 1) {
                        switch(args[1]) {
                            case "names":
                                ColoredNames.loadNames();
                                ColoredNames.textCache.clear();
                                ColoredNames.regexCounter = 0;
                                Utils.sendModMessage("Colored names refreshed");
                                break;
                            case "summons":
                                SummonUtils.loadSkins();
                                Utils.sendModMessage("Summon skins refreshed");
                                break;
                        }
                    }
                    break;

                case "debug":
                    if(args.length > 1) {
                        switch(args[1]) {
                            case "dungeon":
                                DungeonUtils.debug();
                                break;
                            case "names":
                                ColoredNames.debug();
                                break;
                        }
                    }
                    break;

                case "addName":
                    if(args.length > 2) {
                        ColoredNames.users.put(args[1], args[2]);
                    }
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
    public int getRequiredPermissionLevel() {
        return 0;
    }

}
