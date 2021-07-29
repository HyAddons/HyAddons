package com.jeromepaulos.hyaddons.features.misc;

import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FixCoop {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if(Utils.inSkyBlock && event.toolTip != null && Utils.getInventoryName().equals("SkyBlock Menu")) {
            for(int i = 0; i < event.toolTip.size(); i++) {
                if(event.toolTip.get(i).equals("§5§o§eClick to hide rankings!")) {
                    event.toolTip.set(i, "§5§o§eClick to view!");
                }
            }
        }

        /*if(Utils.inSkyBlock && event.toolTip != null && Utils.getInventoryName().endsWith("Collection")) {
            for(int i = 0; i < event.toolTip.size(); i++) {
                String line = event.toolTip.get(i);
                if(line.contains("Co-op Contributions:")) {
                    line = event.toolTip.get(i++);
                    while(!line.equals("§5§o") && i < event.toolTip.size()) {
                        if(line.endsWith("§7: §e0")) event.toolTip.remove(i);
                        line = event.toolTip.get(i++);
                    }
                    break;
                }
            }
        }*/
    }

}
