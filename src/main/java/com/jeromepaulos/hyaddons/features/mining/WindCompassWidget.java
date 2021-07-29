package com.jeromepaulos.hyaddons.features.mining;

import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.gui.GuiWidget;
import com.jeromepaulos.hyaddons.utils.ScoreboardUtils;
import com.jeromepaulos.hyaddons.gui.WidgetPosition;

import java.util.List;

public class WindCompassWidget extends GuiWidget {

    public WindCompassWidget() {
        super("Wind Compass", new WidgetPosition(50, 50));
        HyAddons.WIDGET_MANAGER.addWidget(this);
    }

    @Override
    public void renderWidget(WidgetPosition position) {
        List<String> scoreboard = ScoreboardUtils.getScoreboard();
        for(int i = 0; i < scoreboard.size(); i++) {
            String line = scoreboard.get(i);
            if(line.contains("Wind Compass")) {
                line = scoreboard.get(i-1);
            }
        }
    }

    @Override
    public void renderPreview(WidgetPosition position) {

    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
