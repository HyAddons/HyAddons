package com.jeromepaulos.hyaddons.features.dungeons;

import com.jeromepaulos.hyaddons.utils.Utils;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.DungeonUtils;
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

public class DungeonVignette {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static Double tankDistance = null;

    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if(Utils.inDungeon && DungeonUtils.dungeonRun != null && Config.tankVignette) {
            tankDistance = null;
            for(DungeonUtils.DungeonRun.DungeonPlayer player : DungeonUtils.dungeonRun.team.values()) {
                if(player._class == DungeonUtils.Class.TANK) {
                    EntityPlayer tank = mc.theWorld.getPlayerEntityByName(player.username);
                    if(tank != null) {
                        double calcTankDistance = tank.getPosition().distanceSq(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                        if(calcTankDistance <= 30 && (tankDistance == null || calcTankDistance < tankDistance)) tankDistance = calcTankDistance;
                    }
                }
            }
        } else {
            tankDistance = null;
        }
    }

    private static void renderVignette(float blendColor) {
        ScaledResolution scaledRes = new ScaledResolution(mc);
        mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);

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
            if(Config.tankVignette && mc.gameSettings.thirdPersonView == 0 && tankDistance != null) {
                // Change "opacity" of vignette gradually as tank moves away
                renderVignette(tankDistance > 20 ? (float) ((30-tankDistance)/10) : 1);
            }
        }
    }

}
