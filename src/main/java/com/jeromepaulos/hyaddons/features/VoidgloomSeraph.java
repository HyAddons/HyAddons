package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.RenderUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoidgloomSeraph {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean inSepulture = false;
    private static boolean activateFeatures = false;

    private static BlockPos beacon = null;
    private static Integer hits = null;

    private BlockPos findBeacon(int radius) {
        BlockPos foundBeacon = null;
        if(mc.theWorld != null) {
            for(int x = radius*-1; x <= radius; x++) {
                for(int y = radius*-1; y <= radius; y++) {
                    for(int z = radius*-1; z <= radius; z++) {

                        double coordsX = x + mc.thePlayer.posX;
                        double coordsY = y + mc.thePlayer.posY;
                        double coordsZ = z + mc.thePlayer.posZ;

                        BlockPos position = new BlockPos(coordsX, coordsY, coordsZ);
                        Block block = mc.theWorld.getBlockState(position).getBlock();

                        if(block == Blocks.beacon) {
                            foundBeacon = position;
                        }
                    }
                }
            }
        }
        return foundBeacon;
    }

    private int ticks = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Utils.inSkyBlock) {
            inSepulture = Utils.scoreboardContains("Void Sepulture");
            boolean questActive = Utils.scoreboardContains("Voidgloom Seraph");

            if(Config.ignoreOtherVoidgloom) {
                // only active if your boss is alive
                activateFeatures = questActive && Utils.scoreboardContains("Slay the boss!");
            } else {
                // active all the time
                activateFeatures = true;
            }

            if(ticks % 4 == 0) {
                if(inSepulture && activateFeatures) {
                    if(Config.highlightVoidgloomBeacons) {
                        beacon = findBeacon(Config.voidgloomSearchRadius);
                    }

                    if(Config.showVoidgloomHits) {
                        List<EntityArmorStand> armorStands = mc.theWorld.getEntitiesWithinAABB(EntityArmorStand.class, mc.thePlayer.getEntityBoundingBox().expand(Config.voidgloomSearchRadius, Config.voidgloomSearchRadius, Config.voidgloomSearchRadius));
                        for(EntityArmorStand stand : armorStands) {
                            if(stand.hasCustomName()) {
                                String name = Utils.removeFormatting(stand.getCustomNameTag());
                                if(name.contains("Voidgloom Seraph")) {
                                    Matcher matcher = Pattern.compile("☠ Voidgloom Seraph (\\d{1,3}) Hits?").matcher(name);
                                    if(matcher.matches()) {
                                        hits = Integer.parseInt(matcher.group(1));
                                    } else {
                                        hits = null;
                                    }
                                } else {
                                    hits = null;
                                }
                            }
                        }
                    }
                }
                ticks = 0;
            }
            ticks++;
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if(Config.highlightVoidgloomBeacons && beacon != null && inSepulture && activateFeatures) {
            Color color = new Color(0,255,0, 128);
            RenderUtils.highlightBlock(beacon, color, event.partialTicks);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if(event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            if(Config.showVoidgloomHits && inSepulture && activateFeatures && hits != null) {
                String hitString = hits.toString();
                ScaledResolution sr = new ScaledResolution(mc);
                int x = (sr.getScaledWidth()-mc.fontRendererObj.getStringWidth(hitString))/2;
                int y = sr.getScaledHeight()/2+10;
                mc.fontRendererObj.drawString(hitString, x, y, Color.WHITE.getRGB());
            }
        }
    }

}
