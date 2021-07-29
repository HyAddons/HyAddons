package com.jeromepaulos.hyaddons.features.misc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeromepaulos.hyaddons.utils.HttpUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColoredNames {

    public static HashMap<String, String> users = new HashMap<>();
    public static HashMap<String, String> textCache = new HashMap<>();
    public static int regexCounter = 0;

    public static void loadNames() {
        new Thread(() -> {
            String response = HttpUtils.fetch("https://raw.githubusercontent.com/HyAddons/HyAddons-data/main/names.json");
            if(response != null) {
                Gson gson = new Gson();
                Type stringStringMap = new TypeToken<HashMap<String, String>>(){}.getType();
                users = gson.fromJson(response, stringStringMap);
            }
        }, "HyAddons-Names").start();
    }

    public static String replaceString(String text) {
        for (Map.Entry<String, String> user : users.entrySet()) {
            if(text.contains(user.getKey())) {
                text = replaceName(text, user.getKey(), user.getValue());
            }
        }
        return text;
    }

    public static String replaceName(String text, String name, String color) {
        regexCounter++;

        if(text.contains("MVP") || text.contains("VIP")) {
            String regex = "(§[0-9a-f])\\[(VIP|MVP)([§0-9a-fr+]*)?\\] "+name;
            String replacement = "§"+color+"[$2$3§"+color+"] "+name;

            Matcher matcher = Pattern.compile(regex).matcher(text);
            text = matcher.replaceAll(replacement);
        }
        text = replaceStringNoRank(text, name, color);

        return text;
    }

    public static void debug() {
        Utils.sendDebugMessage("Text Cache: "+Utils.formatNumber(textCache.size())+" items");
        Utils.sendDebugMessage("Name Cache: "+Utils.formatNumber(users.size())+" names");
        Utils.sendDebugMessage("Regex Runs: "+Utils.formatNumber(regexCounter));
    }

    public static String replaceStringNoRank(String text, String name, String color) {
        char[] characters = text.toCharArray();

        char currentColor = 'f';
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < characters.length; i++) {
            char character = characters[i];

            if(character == '§' && i+1 < characters.length) {
                currentColor = characters[i+1];
            }

            if(i + name.length() <= characters.length) {
                char[] possibleName = Arrays.copyOfRange(characters, i, i + name.length());
                if(Arrays.equals(possibleName, name.toCharArray())) {
                    output.append("§").append(color).append(name).append("§").append(currentColor);
                    i += name.length() - 1;
                    continue;
                }
            }

            output.append(character);
        }

        return output.toString();
    }

    @SubscribeEvent
    public void onWorldJoin(WorldEvent.Load event) {
        textCache.clear();
        regexCounter = 0;
    }

}