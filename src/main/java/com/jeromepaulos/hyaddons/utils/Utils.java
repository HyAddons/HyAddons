package com.jeromepaulos.hyaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class Utils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean inSkyBlock = false;
    public static boolean inDungeon = false;

    public static String removeFormatting(String input) {
        return input.replaceAll("[§|&][0-9a-fk-or]", "");
    }

    public static void sendMessage(String message) {
        if(!message.contains("§")) {
            message = message.replace("&", "§");
        }
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static boolean scoreboardContains(String string) {
        boolean result = false;
        List<String> scoreboard = ScoreboardUtils.getSidebarLines();
        for (String line : scoreboard) {
            line = ScoreboardUtils.cleanSB(line);
            if(line.contains(string)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static void sendModMessage(String message) {
        sendMessage("&dHyAddons > &f" + message);
    }
    public static void sendDebugMessage(String message) {
        sendMessage("&dHyAddons Debug > &f" + message);
    }

    public static void displayTitle(String title, String subtitle, int ticks) {
        GuiIngame gui = mc.ingameGUI;
        gui.displayTitle(title, null, 0, ticks, 0);
        gui.displayTitle(null, subtitle, 0, ticks, 0);
        gui.displayTitle(null, null, 0, ticks, 0);
    }

    public static boolean listContainsString(Iterable<String> list, String string) {
        boolean contains = false;
        for(String item : list) {
            if(item.contains(string)) {
                contains = true;
                break;
            }
        }
        return contains;
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

                inDungeon = inSkyBlock && scoreboardContains("The Catacombs");

                ticks = 0;
            }
            ticks++;
        }
    }
}
