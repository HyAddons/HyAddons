package com.jeromepaulos.hyaddons.updates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.HttpUtils;

public class Updater {

    private static String latestVersion = null;

    public Updater() {
        if(Config.updateType != 0) {
            new Thread(() -> {
                String response = HttpUtils.fetch("https://projects.jeromepaulos.com/hyaddons/updates/");

                if(response != null) {
                    JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                    JsonElement latest = json.get("latest");
                    JsonElement stable = json.get("stable");

                    if(Config.updateType == 1) { // latest
                        latestVersion = latest.getAsString();
                    } else if(Config.updateType == 2 && stable != null) { // stable
                        latestVersion = stable.getAsString();
                    }

                    if(latestVersion != null && !HyAddons.VERSION.equals(latestVersion)) {
                        if(Config.updateType != 2 && !latestVersion.toLowerCase().contains("pre")) {
                            HyAddons.update = latestVersion;
                        }
                    }
                }
            }, "HyAddons-Updater").start();
        }
    }

}
