package com.jeromepaulos.hyaddons.features.dungeons;

import com.jeromepaulos.hyaddons.utils.Utils;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.DungeonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class NecronPhases {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static final int[][][] coords = {
        { {250,259}, {240,269} }, // yellow[0], north[0]/south[1], x[0]/z[1]
        { {250,235}, {240,245} }, // green[1], north[0]/south[1], x[0]/z[1]
        { {304,259}, {294,269} } // purple[2], north[0]/south[1], x[0]/z[1]
    };

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if(Config.necronPhaseAnnouncements && DungeonUtils.inFloor(DungeonUtils.Floor.FLOOR_7) && event.type == 0) {
            String message = event.message.getUnformattedText();
            if(message.startsWith("[BOSS] Necron:")) {
                if(message.contains("You tricked me!") || message.contains("That beam, it hurts! IT HURTS!!")) {
                    Utils.displayTitle(EnumChatFormatting.RED+"NECRON LASERED", null, 60);
                } else if(message.contains("OOF") || message.contains("STOP USING MY FACTORY AGAINST ME!") || message.contains("ANOTHER TRAP!! YOUR TRICKS ARE FUTILE!") || message.contains("SERIOUSLY? AGAIN?!") || message.contains("STOP!!!!!")) {

                    List<EntityArmorStand> entities = mc.theWorld.getEntities(EntityArmorStand.class, entity -> {
                        if(entity.hasCustomName()) {
                            return entity.getCustomNameTag().contains("Necron");
                        } else {
                            return false;
                        }
                    });

                    if(entities.size() > 0) {
                        EntityArmorStand necron = entities.get(0);

                        double x = Math.floor(necron.posX);
                        double z = Math.floor(necron.posZ);

                        if(x<coords[0][0][0] && x>coords[0][1][0] && z<coords[0][1][1] && z>coords[0][0][1]) {
                            Utils.displayTitle(EnumChatFormatting.YELLOW+"YELLOW PILLAR", null, 60);
                        } else if(x<coords[1][0][0] && x>coords[1][1][0] && z<coords[1][1][1] && z>coords[1][0][1]) {
                            Utils.displayTitle(EnumChatFormatting.DARK_GREEN+"GREEN PILLAR", null, 60);
                        } else if(x<coords[2][0][0] && x>coords[2][1][0] && z<coords[2][1][1] && z>coords[2][0][1]) {
                            Utils.displayTitle(EnumChatFormatting.DARK_PURPLE+"PURPLE PILLAR", null, 60);
                        } else {
                            Utils.displayTitle(EnumChatFormatting.WHITE+"NECRON SQUASHED", null, 60);
                        }
                    }

                } else if(message.contains("I'VE HAD ENOUGH! YOU'RE NOT HITTING ME WITH ANY MORE PILLARS!")) {
                    Utils.displayTitle(EnumChatFormatting.RED+"RED PILLAR", null, 60);
                } else if(message.contains("ARGH!")) {
                    Utils.displayTitle(EnumChatFormatting.RED+"EXPLOSION OVER", null, 60);
                }
            }
        }
    }

}
