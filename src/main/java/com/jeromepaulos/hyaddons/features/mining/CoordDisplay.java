package com.jeromepaulos.hyaddons.features.mining;

import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.gui.GuiWidget;
import com.jeromepaulos.hyaddons.gui.WidgetPosition;
import com.jeromepaulos.hyaddons.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class CoordDisplay extends GuiWidget {

    public CoordDisplay() {
        super("Coord Display", new WidgetPosition(5, 30));
        HyAddons.WIDGET_MANAGER.addWidget(this);
    }

    @Override
    public void renderWidget(WidgetPosition position) {
        if(Config.coordinateDisplay && Minecraft.getMinecraft().thePlayer != null) {
            BlockPos pos = Minecraft.getMinecraft().thePlayer.getPosition();
            RenderUtils.drawString("§f"+pos.getX()+"§7/§f"+pos.getY()+"§7/§f"+pos.getZ(), position);
        }
    }

    @Override
    public void renderPreview(WidgetPosition position) {
        RenderUtils.drawString("§f124§7/§f23§7/§f7453", position);
    }

    @Override
    public int getWidth() {
        return RenderUtils.getStringWidth("§f124§7/§f23§7/§f7453");
    }

    @Override
    public int getHeight() {
        return RenderUtils.getLineHeight();
    }

    @Override
    public boolean isEnabled() {
        return Config.coordinateDisplay;
    }
}
