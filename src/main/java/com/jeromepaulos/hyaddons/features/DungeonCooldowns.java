package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class DungeonCooldowns {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static Long cdEndTime = null;

    private void displayCooldown(int duration) {
        duration += 1;
        cdEndTime = System.currentTimeMillis() + duration*1000L;
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
                }
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if(event.type == 0) {
            String message = Utils.removeFormatting(event.message.getUnformattedText());

            switch(message) {
                case "Used Explosive Shot!":
                    if(Config.explosiveShotCooldown && Utils.inDungeon) {
                        displayCooldown(40);
                    }

                case "Used Seismic Wave!":
                    if(Config.seismicWaveCooldown && Utils.inDungeon) {
                        if(mc.thePlayer.getHeldItem().getDisplayName().contains("Earth Shard")) {
                            displayCooldown(13);
                        } else {
                            displayCooldown(15);
                        }
                    }
            }
        }
    }

}
