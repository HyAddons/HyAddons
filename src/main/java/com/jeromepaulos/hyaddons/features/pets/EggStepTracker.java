package com.jeromepaulos.hyaddons.features.pets;

import com.jeromepaulos.hyaddons.utils.TooltipUtils;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EggStepTracker {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (Config.prehistoricEggSteps && event.itemStack.hasTagCompound()) {
            NBTTagCompound extraAttributes = event.itemStack.getSubCompound("ExtraAttributes", false);
            if(extraAttributes != null && extraAttributes.hasKey("blocks_walked")) {
                int steps = extraAttributes.getInteger("blocks_walked");
                if(steps <= 100000) {
                    String stepsString = Utils.formatNumber(steps);
                    TooltipUtils.add(event, "§7Blocks Walked: §d"+stepsString+"§7/100k", TooltipUtils.Position.BELOW_RARITY);
                }
            }
        }
    }

}
