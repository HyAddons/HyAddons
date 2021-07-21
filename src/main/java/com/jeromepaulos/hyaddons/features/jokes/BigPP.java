package com.jeromepaulos.hyaddons.features.jokes;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BigPP {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTooltip(ItemTooltipEvent event) {
        for(int i = 0; i < event.toolTip.size(); i++) {
            event.toolTip.set(i, event.toolTip.get(i).replace("§9Smarty Pants V", "§9Big PP V"));
        }
    }

}
