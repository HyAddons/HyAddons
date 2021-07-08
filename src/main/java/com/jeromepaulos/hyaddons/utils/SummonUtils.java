package com.jeromepaulos.hyaddons.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class SummonUtils {

    public static HashMap<String, String> skins = null;

    public static void loadSkins() {
        new Thread(() -> {
            String response = HttpUtils.fetch("https://raw.githubusercontent.com/HyAddons/HyAddons-data/main/summons.json");
            if(response != null) {
                Gson gson = new Gson();
                Type stringStringMap = new TypeToken<HashMap<String, String>>(){}.getType();
                skins = gson.fromJson(response, stringStringMap);
            }
        }, "HyAddons-Summons").start();
    }

}
