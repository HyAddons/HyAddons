package com.jeromepaulos.hyaddons.updates;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jeromepaulos.hyaddons.HyAddons;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

public class Updater {

    private static final String url = "https://api.github.com/repos/jxxe/HyAddons/releases/latest";
    private static String response = null;

    public Updater() {
        new Thread(() -> {
            try {
                response = IOUtils.toString(new URL(url));
            } catch (IOException ignored) {}

            if(response != null) {
                JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                String latestVersion = json.get("tag_name").getAsString();

                if(!HyAddons.VERSION.equals(latestVersion)) {
                    HyAddons.update = latestVersion;
                }
            }

        }, "HyAddons-Updater").start();
    }

    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if(HyAddons.update != null) {
            if(Minecraft.getMinecraft().thePlayer != null) {
                ChatComponentText updateMessage = new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE+"HyAddons > "+EnumChatFormatting.YELLOW+"Click here "+EnumChatFormatting.WHITE+" to update to version"+HyAddons.update+"!");
                ChatStyle clickAction = new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/jxxe/HyAddons/releases/latest"));
                clickAction.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(EnumChatFormatting.YELLOW+"https://github.com/jxxe/HyAddons/releases/latest")));
                updateMessage.setChatStyle(clickAction);
                Minecraft.getMinecraft().thePlayer.addChatMessage(updateMessage);
                HyAddons.update = null;
            }
        }
    }

}
