package com.jeromepaulos.hyaddons.updates;

import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class UpdateGui extends GuiScreen {

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, this.width / 2 - 105, this.height / 2, 100, 20, "Update"));
        buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height / 2, 100, 20, "Ignore"));
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if(button.id == 0) {
            Utils.openUrl("https://github.com/jxxe/HyAddons/releases/tag/"+ HyAddons.update);
            this.mc.shutdown();
        } else if(button.id == 1) {
            HyAddons.guiToOpen = new GuiMainMenu();
            HyAddons.update = null;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawCustomBackground();
        String title = "HyAddons "+EnumChatFormatting.LIGHT_PURPLE+"v"+HyAddons.update+EnumChatFormatting.WHITE+" is available!";
        int titleWidth = mc.fontRendererObj.getStringWidth(title);
        mc.fontRendererObj.drawStringWithShadow(
                title,
                (this.width - titleWidth) / 2f,
                this.height / 2f - 20,
                Color.WHITE.getRGB()
        );
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCustomBackground() {
        ResourceLocation texture = new ResourceLocation("textures/blocks/cobblestone.png");
        int tint = 0;
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, (double)this.height, 0.0D).tex(0.0D, (double)((float)this.height / 32.0F + (float)tint)).color(64, 64, 64, 255).endVertex();
        worldrenderer.pos((double)this.width, (double)this.height, 0.0D).tex((double)((float)this.width / 32.0F), (double)((float)this.height / 32.0F + (float)tint)).color(64, 64, 64, 255).endVertex();
        worldrenderer.pos((double)this.width, 0.0D, 0.0D).tex((double)((float)this.width / 32.0F), (double)tint).color(64, 64, 64, 255).endVertex();
        worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)tint).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }

}
