package com.jeromepaulos.hyaddons.gui;

import com.jeromepaulos.hyaddons.HyAddons;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.HashMap;
import java.util.Map;

public class MoveWidgetGui extends GuiScreen {

    private int lastMouseX;
    private int lastMouseY;

    private GuiWidget moving = null;
    private HashMap<String, MoveButton> buttonMap = new HashMap<>();

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mouseMoved(mouseX, mouseY);
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
        for(GuiWidget widget : HyAddons.WIDGET_MANAGER.getWidgets().values()) {
            if(widget.isEnabled()) {
                MoveButton moveButton = new MoveButton(widget);
                this.buttonList.add(moveButton);
                buttonMap.put(widget.getName(), moveButton);
            }
        }
    }

    public void mouseMoved(int mouseX, int mouseY) {
        int xMoved = mouseX - lastMouseX;
        int yMoved = mouseY - lastMouseY;

        if(moving != null) {
            moving.setX(moving.getX() + xMoved);
            moving.setY(moving.getY() + yMoved);
            HyAddons.WIDGET_MANAGER.setWidget(moving.getName(), moving);
            this.buttonList.clear();
            initGui();
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if(button instanceof MoveButton) {
            for(Map.Entry<String, MoveButton> moveButton : buttonMap.entrySet()) {
                if(button == moveButton.getValue()) {
                    moving = HyAddons.WIDGET_MANAGER.getWidgets().get(moveButton.getKey());
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        moving = null;
    }

    @Override
    public void onGuiClosed() {
        moving = null;
        HyAddons.WIDGET_MANAGER.save();
    }
}
