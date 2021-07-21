package com.jeromepaulos.hyaddons.gui;

import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class WidgetManager {

    private static File configFile;
    private static HashMap<String, GuiWidget> widgets = new HashMap<>();
    private static HashMap<String, GuiWidget> widgetsUnmodified = new HashMap<>();

    public WidgetManager() {
        try {
            configFile = new File("./config/hyaddons-gui.text");
            if(!configFile.isFile() && !configFile.createNewFile()) {
                throw new Exception("Error creating GUI config file");
            }
        } catch(Exception error) {
            System.out.println(error.getMessage());
        }
    }

    public void addWidget(GuiWidget widget) {
        widgets.put(widget.getName(), widget);
        widgetsUnmodified.put(widget.getName(), widget);
    }

    public void load() {
        try {
            Files.lines(configFile.toPath()).forEach(line -> {
                if(line.contains(":") && line.contains(",")) {
                    String[] splitLine = line.split(":");
                    String[] splitPosition = splitLine[1].split(",");
                    GuiWidget lineWidget = widgets.get(splitLine[0]);
                    lineWidget.setPosition(new WidgetPosition(
                            Integer.parseInt(splitPosition[0]),
                            Integer.parseInt(splitPosition[1])
                    ));
                    widgets.replace(splitLine[0], lineWidget);
                }
            });
        } catch (Exception error) {
            System.out.println("Error reading GUI config file");
            error.printStackTrace();
        }
    }

    public void save() {
        try {
            StringBuilder data = new StringBuilder();
            for(Map.Entry<String, GuiWidget> widget : widgets.entrySet()) {
                WidgetPosition widgetPosition = widget.getValue().getPosition();
                data
                        .append(widget.getKey())
                        .append(":")
                        .append(widgetPosition.toString())
                        .append(System.getProperty("line.separator"));
            }
            Files.write(configFile.toPath(), data.toString().getBytes(StandardCharsets.UTF_8));
        } catch(Exception error) {
            Utils.sendDebugMessage("Error saving GUI config file. Please report this.");
            System.out.println("Error saving GUI config file");
        }
    }

    public HashMap<String, GuiWidget> getWidgets() {
        return widgets;
    }

    public void setWidget(String widgetName, GuiWidget widget) {
        widgets.replace(widgetName, widget);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if(event.type == RenderGameOverlayEvent.ElementType.HOTBAR && widgets != null) {
            for(GuiWidget widget : widgets.values()) {
                if(widget.isEnabled() && !(Minecraft.getMinecraft().currentScreen instanceof MoveWidgetGui)) {
                    widget.renderWidget(widget.getPosition());
                }
            }
        }
    }

}
