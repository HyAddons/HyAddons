package com.jeromepaulos.hyaddons.gui;

public abstract class GuiWidget {

    private String name;
    private WidgetPosition position;

    public GuiWidget(String name, WidgetPosition position) {
        this.name = name;
        this.position = position;
    }

    public abstract void renderWidget(WidgetPosition position);
    public abstract void renderPreview(WidgetPosition position);

    public abstract int getWidth();
    public abstract int getHeight();

    public abstract boolean isEnabled();

    public String getName() { return name; }

    public WidgetPosition getPosition() { return position; }
    public int getX() { return getPosition().getX(); }
    public int getY() { return getPosition().getY(); }

    public void setPosition(WidgetPosition position) { this.position = position; }
    public void setX(int x) { this.position = new WidgetPosition(x, getY()); }
    public void setY(int y) { this.position = new WidgetPosition(getX(), y); }

}
