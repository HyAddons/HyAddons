package com.jeromepaulos.hyaddons.features.misc;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CakeSoul {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event) {
        if(Config.showCakeSoulOwner && Utils.inSkyBlock) {
            ItemStack item = event.itemStack;
            NBTTagCompound nbt = item.getTagCompound();

            if(nbt.hasKey("ExtraAttributes")) {
                String cakeOwner = nbt.getCompoundTag("ExtraAttributes").getString("cake_owner");
                String capturedPlayer = nbt.getCompoundTag("ExtraAttributes").getString("captured_player");
                String itemId = nbt.getCompoundTag("ExtraAttributes").getString("id");

                if(itemId.equals("CAKE_SOUL")) {
                    if(capturedPlayer.equals("")) {
                        if(cakeOwner.equals("")) {
                            event.toolTip.add(4, EnumChatFormatting.GRAY + "Owner: Not recorded");
                        } else {
                            event.toolTip.add(4, EnumChatFormatting.GRAY + "Owner: " + cakeOwner);
                        }
                    }
                }
            }
        }
    }

}
