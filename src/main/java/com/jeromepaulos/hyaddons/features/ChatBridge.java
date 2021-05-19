package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatBridge {

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if(Config.enableBridge && !Config.bridgeBotUsername.equals("")) {
            String message = Utils.removeFormatting(event.message.getUnformattedText());

            if(message.startsWith("Guild >")) {
                Pattern regex = Pattern.compile("Guild > (?:\\\\[.*\\\\] )?(.*)(?: \\\\[.*\\\\])?: (.*): (.*)");
                Matcher matcher = regex.matcher(message);

                if(matcher.find()) {
                    if(matcher.group(1).contains(Config.bridgeBotUsername)) {
                        event.setCanceled(true);

                        char[] colors = {'1', '2', '3', '4', '5', '6', 'f', '7', '9', 'a', 'b', 'c', 'd', 'e'};
                        String[] prefixes = {"[DISCORD]", "[DISC]", "[D]", "[BRIDGE]"};

                        String replacementMessage = "&2Guild > &" + colors[Config.bridgeMessageColor] + prefixes[Config.bridgePrefix] + " " + matcher.group(2) + "&f: " + matcher.group(3);
                        Utils.displayMessage(replacementMessage);
                    }
                }
            }
        }
    }

}
