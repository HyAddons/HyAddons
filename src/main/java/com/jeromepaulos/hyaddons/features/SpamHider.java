package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpamHider {

    private static final String joinMessage = "(Guild|Friend) > \\w* (joined|left).";
    private static final String pickaxeAbility = "(Mining Speed Boost|Pickobulus) is now available!";

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = Utils.removeFormatting(event.message.getUnformattedText());

        if(message.matches(joinMessage)) {
            if(message.startsWith("Friend >") && Config.friendJoinLeaveMessages) {
                event.setCanceled(true);
            } else if(message.startsWith("Guild >") && Config.guildJoinLeaveMessages) {
                event.setCanceled(true);
            }
        } else if(message.matches(pickaxeAbility) && Config.pickaxeAbilityMessages) {
            event.setCanceled(true);
        }

    }

}
