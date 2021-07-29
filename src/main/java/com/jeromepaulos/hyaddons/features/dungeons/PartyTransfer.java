package com.jeromepaulos.hyaddons.features.dungeons;

import com.jeromepaulos.hyaddons.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class PartyTransfer extends CommandBase {

    @Override
    public String getCommandName() {
        return "pt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(Config.shortPartyTransfer) {
            if(args.length > 0) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/party transfer "+args[0]);
            } else {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/playtime");
            }
        } else {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/playtime");
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

}