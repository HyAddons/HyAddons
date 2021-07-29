package com.jeromepaulos.hyaddons.utils;

import com.jeromepaulos.hyaddons.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.net.URI;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean inSkyBlock = false;
    public static boolean inDungeon = false;

    public static String removeFormatting(String input) {
        return input.replaceAll("[§|&][0-9a-fk-or]", "");
    }

    public static String formatNumber(long number) {
        return NumberFormat.getInstance(Locale.US).format(number);
    }

    public static void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch(Exception ignored) {}
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
    
    public static void sendDebugMessage(String message) {
        if(Config.showDebugMessages) {
            sendMessage("&dHyAddons Debug > &f" + message);
        }
    }

    public static void displayTitle(String title, String subtitle, int ticks) {
        mc.ingameGUI.displayTitle(title, subtitle, 0, ticks, 0);
    }

    public static String getInventoryName() {
        String inventoryName = mc.thePlayer.openContainer.inventorySlots.get(0).inventory.getName();
        if(inventoryName == null) return "null";
        return inventoryName;
    }

    public static String getSkyBlockID(ItemStack item) {
        NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
        if(extraAttributes != null && extraAttributes.hasKey("id")) {
            return extraAttributes.getString("id");
        }
        return null;
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

    public static int romanToInt(String s) {
        Map<Character, Integer> numerals = new HashMap<>();
        numerals.put('I', 1);
        numerals.put('V', 5);
        numerals.put('X', 10);
        numerals.put('L', 50);
        numerals.put('C', 100);
        numerals.put('D', 500);
        numerals.put('M', 1000);

        int result = 0;
        for(int i = 0; i < s.length(); i++) {
            int add = numerals.get(s.charAt(i));
            if(i < s.length() - 1) {
                int next = numerals.get(s.charAt(i + 1));
                if(next / add == 5 || next / add == 10) {
                    add = next - add;
                    i++;
                }
            }
            result = result + add;
        }

        return result;
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

                inDungeon = inSkyBlock && ScoreboardUtils.scoreboardContains("The Catacombs");

                ticks = 0;
            }
            ticks++;
        }
    }
}
