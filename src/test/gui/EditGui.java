package com.jeromepaulos.hyaddons.gui;

import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class EditGui extends GuiScreen {

    @Override
    public void initGui() {
        super.initGui();
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 125).getRGB());

    }

}
