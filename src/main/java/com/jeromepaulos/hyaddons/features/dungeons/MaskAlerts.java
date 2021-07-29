package com.jeromepaulos.hyaddons.features.dungeons;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MaskAlerts {

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if(event.type == 0 && Config.maskAlert) {
            String message = event.message.getUnformattedText();

            if(message.startsWith("Your ") && message.endsWith("Bonzo's Mask saved your life!")) {
                Utils.sendDebugMessage("Bonzo Mask detected");
                Utils.displayTitle("§9BONZO MASK", null, 20);
            }

            if(message.equals("Second Wind Activated! Your Spirit Mask saved your life!")) {
                Utils.sendDebugMessage("Spirit Mask detected");
                Utils.displayTitle("§6SPIRIT MASK", null, 20);
            }
        }
    }

}
