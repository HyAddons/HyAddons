package com.jeromepaulos.hyaddons.gui;

public class WidgetPosition {

    private Integer xPosition = null;
    private Integer yPosition = null;

    public WidgetPosition(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    public String toString() {
        return xPosition + "," + yPosition;
    }

}
