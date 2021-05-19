package com.jeromepaulos.hyaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Utils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean inSkyBlock = false;
    private static int ticks = 0;

    public static String removeFormatting(String input) {
        return input.replaceAll("[§|&][0-9,a-f,k-o,r]", "");
    }

    public static void displayMessage(String message) {
        if(!message.contains("§")) {
            message = message.replace("&", "§");
        }
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(mc.thePlayer != null) {
            if(ticks % 20 == 0) {
                ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
                if (scoreboardObj != null) {
                    String scObjName = removeFormatting(scoreboardObj.getDisplayName());
                    if (scObjName.contains("SKYBLOCK")) {
                        inSkyBlock = true;
                        return;
                    }
                }
                ticks = 0;
            }
            inSkyBlock = false;
            ticks++;
        }
    }
}
