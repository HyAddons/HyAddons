package com.jeromepaulos.hyaddons.features.dungeons;

import com.jeromepaulos.hyaddons.utils.Utils;
import com.jeromepaulos.hyaddons.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class DungeonCooldowns {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static Long cdEndTime = null;
    private static String cdSource = null;

    private void displayCooldown(String source, int duration) {
        duration += 1;
        cdEndTime = System.currentTimeMillis() + duration*1000L;
        cdSource = source;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if(event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            if(cdEndTime != null) {
                if(cdEndTime >= System.currentTimeMillis()) {
                    int cdAmount = Math.round((cdEndTime-System.currentTimeMillis())/1000);
                    String cdString = Integer.toString(cdAmount);

                    ScaledResolution sr = new ScaledResolution(mc);
                    int x = (sr.getScaledWidth()-mc.fontRendererObj.getStringWidth(cdString))/2;
                    int y = sr.getScaledHeight()/2+10;

                    mc.fontRendererObj.drawString(cdString, x, y, Color.WHITE.getRGB());
                } else {
                    cdEndTime = null;
                    cdSource = null;
                }
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = Utils.removeFormatting(event.message.getUnformattedText());

        if(event.type == 0) {
            switch(message) {
                case "Used Explosive Shot!":
                    if(Config.explosiveShotCooldown && Utils.inDungeon) {
                        displayCooldown("Explosive Shot", 40);
                    }

                case "Used Seismic Wave!":
                    if(Config.seismicWaveCooldown && Utils.inDungeon) {
                        if(mc.thePlayer.getHeldItem().getDisplayName().contains("Earth Shard")) {
                            displayCooldown("Seismic Wave", 13);
                        } else {
                            displayCooldown("Seismic Wave", 15);
                        }
                    }
            }
        } else if(event.type == 2) {
            if(Config.witherShieldCooldown && message.contains("Wither Impact")) {
                if(cdSource == null || !cdSource.equals("Wither Impact")) {
                    displayCooldown("Wither Impact", 5);
                }
            }
        }
    }

}
