package com.jeromepaulos.hyaddons.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

/**
 * Inspired by Danker's Skyblock Mod under GPL 3.0 license
 * https://github.com/bowser0000/SkyblockMod/blob/master/LICENSE
 */
public class MoveButton extends GuiButton {

    private GuiWidget widget;

    public MoveButton(WidgetPosition position, int width, int height) {
        super(0, position.getX(), position.getY(), width, height, "");
    }

    public MoveButton(GuiWidget widget) {
        super(0, widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), "");
        this.widget = widget;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        int right = widget.getX()+widget.getWidth();
        int bottom = widget.getY()+widget.getHeight();

        boolean hovered = mouseX > widget.getX() && mouseX < right && mouseY > widget.getY() && mouseY < bottom;
        int color = new Color(211,211,211, hovered ? 89 : 64).getRGB();

        drawRect(widget.getX(), widget.getY(), right, bottom, color);
        widget.renderPreview(widget.getPosition());
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {}

}
