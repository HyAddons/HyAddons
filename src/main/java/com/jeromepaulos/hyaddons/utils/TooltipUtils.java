package com.jeromepaulos.hyaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class TooltipUtils {

    public static Integer insertAt = null;

    public enum Position {
        ABOVE_RARITY, BELOW_RARITY;
    }

    /**
     * Modified from SkyblockAddons under MIT License
     * https://github.com/BiscuitDevelopment/SkyblockAddons/blob/master/LICENSE
     */
    public static ItemTooltipEvent add(ItemTooltipEvent event, String text, Position position) {
        insertAt = event.toolTip.size();
        if(position == Position.ABOVE_RARITY) insertAt--; // 1 line for the rarity
        if(Minecraft.getMinecraft().gameSettings.advancedItemTooltips) {
            insertAt -= 2; // 1 line for the item name, and 1 line for the nbt
            if(event.itemStack.isItemDamaged()) {
                insertAt--; // 1 line for damage
            }
        }

        event.toolTip.add(insertAt++, text);
        return event;
    }

}
