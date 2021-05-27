package com.jeromepaulos.hyaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Utils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean inSkyBlock = false;

    public static String removeFormatting(String input) {
        return input.replaceAll("[§|&][0-9a-fk-or]", "");
    }

    public static void sendMessage(String message) {
        if(!message.contains("§")) {
            message = message.replace("&", "§");
        }
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void sendModMessage(String message) {
        sendMessage("&dHyAddons > &f" + message);
    }

    public static void displayTitle(String title, String subtitle, int ticks, int fade) {
        GuiIngame gui = mc.ingameGUI;
        gui.displayTitle(title, null, fade, ticks, fade);
        gui.displayTitle(null, subtitle, fade, ticks, fade);
        gui.displayTitle(null, null, fade, ticks, fade);
    }

    private static int ticks = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(mc.thePlayer != null && mc.theWorld != null) {
            if(ticks % 20 == 0) {
                ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
                if(scoreboardObj != null) {
                    String scoreboardName = removeFormatting(scoreboardObj.getDisplayName());
                    inSkyBlock = scoreboardName.contains("SKYBLOCK");
                }
                ticks = 0;
            }
            ticks++;
        }
    }
}
