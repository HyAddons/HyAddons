package com.jeromepaulos.hyaddons.features.mining;

import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.gui.GuiWidget;
import com.jeromepaulos.hyaddons.gui.WidgetPosition;
import com.jeromepaulos.hyaddons.utils.LocationUtils;
import com.jeromepaulos.hyaddons.utils.RenderUtils;
import com.jeromepaulos.hyaddons.utils.TabUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommissionsWidget extends GuiWidget {

    private static HashMap<String, String> commissions = new HashMap<>();

    public CommissionsWidget() {
        super("Commission Widget", new WidgetPosition(5, 50));
        HyAddons.WIDGET_MANAGER.addWidget(this);
    }

    @Override
    public void renderWidget(WidgetPosition position) {
        if(isEnabled() && (LocationUtils.onIsland(LocationUtils.Island.DWARVEN_MINES) || LocationUtils.onIsland(LocationUtils.Island.CRYSTAL_HOLLOWS))) {
            StringBuilder commissionsString = new StringBuilder();
            for(Map.Entry<String, String> commission : commissions.entrySet()) {
                commissionsString
                        .append("§9")
                        .append(commission.getKey())
                        .append(" §b")
                        .append(commission.getValue())
                        .append("§r\n");
            }
            RenderUtils.drawString(commissionsString.toString(), position);
        }
    }

    @Override
    public void renderPreview(WidgetPosition position) {
        RenderUtils.drawString("§9Star Sentry Puncher §b9/10\n§9Goblin Slayer §b17/100\n§9Upper Mines Mithril §b74.4%", position);
    }

    @Override
    public int getWidth() {
        return RenderUtils.getStringWidth("§9Star Sentry Puncher §b9/10");
    }

    @Override
    public int getHeight() {
        return RenderUtils.getLineHeight() * 3 + 3;
    }

    @Override
    public boolean isEnabled() {
        return Config.commissionWidget;
    }

    private int counter = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(counter % 10 == 0) {
            if(isEnabled() && (LocationUtils.onIsland(LocationUtils.Island.DWARVEN_MINES) || LocationUtils.onIsland(LocationUtils.Island.CRYSTAL_HOLLOWS))) {
                List<String> tabList = TabUtils.getTabList();
                for(int i = 0; i < tabList.size(); i++) {
                    String name = tabList.get(i);
                    if(name.contains("Commissions")) {
                        commissions.clear();
                        for(int j = i; j < i+4; j++) {
                            if(tabList.size() >= j) {
                                name = tabList.get(j);
                                if((name.contains("%") || name.contains("DONE")) && name.contains(": ")) {
                                    String[] commission = Utils.removeFormatting(name).trim().split(": ");
                                    commissions.put(commission[0], commission[1]);
                                }
                            }
                        }
                        break;
                    }
                }
            }
            counter = 0;
        }
        counter++;
    }

}
