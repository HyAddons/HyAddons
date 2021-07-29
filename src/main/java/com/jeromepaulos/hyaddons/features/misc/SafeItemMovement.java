package com.jeromepaulos.hyaddons.features.misc;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.events.PacketEvent;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SafeItemMovement {

    public static String[] blacklistedItems = new String[]{
            /*"Hyperion",
            "Scylla",
            "Valkyrie",
            "Astraea",
            "Terminator",
            "Plasmaflux Power Orb",
            "Overflux Power Orb",
            "Ice Spray Wand",
            "Daedalus Axe",*/
            "SkyBlock Menu"
    };

    @SubscribeEvent
    public void onOutboundPacket(PacketEvent.SendEvent event) {
        if(Utils.inSkyBlock && Config.safeItemMovement) {
            if(Utils.getInventoryName().contains("Ender Chest") || Utils.getInventoryName().contains("Backpack")) {
                if(event.packet instanceof C0EPacketClickWindow) {
                    C0EPacketClickWindow packet = (C0EPacketClickWindow) event.packet;
                    if(packet.getClickedItem() != null && !isBlacklistedItem(packet.getClickedItem())) {
                        if(packet.getMode() == 0 && packet.getUsedButton() == 0 && Minecraft.getMinecraft().thePlayer.inventory.getItemStack() != null) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    public static boolean isBlacklistedItem(ItemStack item) {
        if(Utils.getSkyBlockID(item) == null) return true;

        String name = item.getDisplayName();
        if(name.equals(" ") || name.equals("")) return true;
        for(String blacklistedItem : blacklistedItems) {
            if(name.contains(blacklistedItem)) {
                return true;
            }
        }

        return false;
    }

}
