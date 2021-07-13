package com.jeromepaulos.hyaddons.features;

public class CakeSoul {

    /*@SubscribeEvent
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
    }*/

}
