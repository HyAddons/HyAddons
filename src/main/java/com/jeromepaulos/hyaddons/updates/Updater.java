package com.jeromepaulos.hyaddons.updates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.config.Config;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Updater {

    private static final String endpoint = "https://projects.jeromepaulos.com/hyaddons/updates/";
    private static String response = null;
    private static String latestVersion = null;

    public Updater() {
        if(Config.updateType != 0) {
            new Thread(() -> {
                try {
                    HttpClient client = HttpClients.createDefault();
                    HttpGet request = new HttpGet(endpoint);
                    request.addHeader("User-Agent", "HyAddons");
                    response = EntityUtils.toString(client.execute(request).getEntity(), "UTF-8");
                } catch (IOException exception) {
                    System.out.println("There was a problem checking for updates: "+exception.getMessage());
                }

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
                        HyAddons.update = latestVersion;
                    }
                }
            }, "HyAddons-Updater").start();
        }
    }

}
