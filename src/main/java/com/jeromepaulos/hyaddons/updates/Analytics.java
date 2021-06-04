package com.jeromepaulos.hyaddons.updates;

/*
Hello! This is the one part of the mod that can seem pretty sketchy, since it's sending information
about your player to us, and it's using a technique commonly used by session ID stealers. However, all
this is doing is sending your username to a Discord webhook so we can keep track of who is using our mod.

Collecting analytics data like this is pretty common among all software. For example, Patcher, Modcore,
and other popular mods by Sk1er, Optifine, and more all do something similar to this.
*/

import com.google.gson.Gson;
import com.jeromepaulos.hyaddons.HyAddons;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.UUID;

public class Analytics {

    private static final String webhookID = "850174339340304435";
    private static final String webhookToken = "-xr3A8rODZBMUYRM87Hb7tjQ5mDl2IvItGjr_4jDk4zzhfK5ckFVc5tKDs29DHxeUGD0";

    private static final String url = "https://discord.com/api/webhooks/"+webhookID+"/"+webhookToken;

    private static class User {
        String content;
        String avatar_url;
        String username;

        public User(UUID uuid, String username) {
            this.content = username+" (`"+uuid+"`) has initialized HyAddons "+HyAddons.VERSION;
            this.avatar_url = "https://mc-heads.net/avatar/"+uuid+"/256";
            this.username = username;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public Analytics() {
        new Thread(() -> {
            GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
            if(profile != null) {
                User user = new User(profile.getId(), profile.getName());
                String data = user.toString();

                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost(url);
                request.addHeader("Content-Type", "application/json");

                try {
                    StringEntity params = new StringEntity(data);
                    request.setEntity(params);
                    httpClient.execute(request);
                } catch(IOException ignored) {}
            }
        }, "HyAddons-Analytics").start();
    }

}
