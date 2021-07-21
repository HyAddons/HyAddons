package com.jeromepaulos.hyaddons.features.dungeons;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.ScoreboardUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonVignette {

    private static final Pattern tankRegex = Pattern.compile("\\[T\\] (.*) .*");
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean protectedByTank = false;
    private static Double tankDistance = null;
    private static String tankUsername = null;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Utils.inDungeon && Config.tankVignette) {
            if(tankUsername == null) {
                String tankLine = ScoreboardUtils.getLineThatContains("[T]");
                if(tankLine != null) {
                    tankLine = Utils.removeFormatting(tankLine);
                    Matcher matcher = tankRegex.matcher(tankLine);
                    if(matcher.matches()) {
                        tankUsername = matcher.group(1);
                    }
                } else {
                    protectedByTank = false;
                }
            } else if(mc.theWorld != null) {
                String tankLine = ScoreboardUtils.getLineThatContains("[T]");
                if(tankLine != null && tankLine.contains("DEAD")) {
                    protectedByTank = false;
                } else {
                    EntityPlayer tank = mc.theWorld.getPlayerEntityByName(tankUsername);
                    if(tank != null) {
                        tankDistance = Math.sqrt(Math.pow(mc.thePlayer.posX-tank.posX, 2) + Math.pow(mc.thePlayer.posY-tank.posY, 2) + Math.pow(mc.thePlayer.posZ-tank.posZ, 2));
                        protectedByTank = tankDistance <= 30;
                    }
                }
            } else {
                tankUsername = null;
            }
        } else {
            protectedByTank = false;
            tankUsername = null;
        }
    }

    public static void renderVignette() {
        ScaledResolution scaledRes = new ScaledResolution(mc);
        mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);

        float blendColor = 1;
        if(tankDistance > 20 && tankDistance < 30) {
            blendColor = (float) ((30-tankDistance)/10);
        }
        GlStateManager.color(blendColor,0,blendColor,0.5F);

        mc.getTextureManager().bindTexture(new ResourceLocation("textures/misc/vignette.png"));
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, (double)scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableBlend();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if(event.type == RenderGameOverlayEvent.ElementType.ALL) {
            if(Config.tankVignette && protectedByTank && mc.gameSettings.thirdPersonView == 0) {
                renderVignette();
            }
        }
    }

}
