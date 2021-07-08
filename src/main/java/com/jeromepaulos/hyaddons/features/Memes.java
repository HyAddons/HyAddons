package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Memes {

    private static int ticksHeld = 0;
    private static boolean mapAlerted = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Utils.inDungeon && !mapAlerted) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            if(player != null && player.getName().equals("Purplez_Gaming") && player.getHeldItem().getDisplayName().contains("Magic Map")) {
                if(ticksHeld > 40) {
                    player.sendChatMessage("I am stupid because I use my handheld map. You should kick me from this party.");
                    mapAlerted = true;
                }
                ticksHeld++;
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        mapAlerted = false;
    }

}
