package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DevTools {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event) {
        if(Config.showSkyBlockId && event.showAdvancedItemTooltips && Utils.inSkyBlock) {
            ItemStack item = event.itemStack;
            NBTTagCompound nbt = item.getTagCompound();

            if(nbt.hasKey("ExtraAttributes")) {
                String itemId = nbt.getCompoundTag("ExtraAttributes").getString("id");
                if(!itemId.equals("")) {
                    event.toolTip.add(EnumChatFormatting.DARK_GRAY + "SkyBlock: " + itemId);
                }
            }
        }
    }

}
