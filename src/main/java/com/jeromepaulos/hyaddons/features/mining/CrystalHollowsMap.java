package com.jeromepaulos.hyaddons.features.mining;

import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.gui.GuiWidget;
import com.jeromepaulos.hyaddons.gui.WidgetPosition;
import com.jeromepaulos.hyaddons.utils.LocationUtils;
import com.jeromepaulos.hyaddons.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class CrystalHollowsMap extends GuiWidget {

    private static final ResourceLocation[] maps = new ResourceLocation[]{
            new ResourceLocation("hyaddons:crystal_hollows/map_labels.png"),
            new ResourceLocation("hyaddons:crystal_hollows/map.png")
    };
    private static final ResourceLocation marker = new ResourceLocation("hyaddons:crystal_hollows/marker.png");

    public CrystalHollowsMap() {
        super("Crystal Hollows Map", new WidgetPosition(5, 17));
        HyAddons.WIDGET_MANAGER.addWidget(this);
    }

    private static WidgetPosition blockPosToMapWidgetPosition(WidgetPosition pos, BlockPos coords) {;
        int x = (int) Math.round(pos.getX() + (MathHelper.clamp_int(coords.getX(), 202, 823) - 202)/621d*128);
        int z = (int) Math.round(pos.getY() + (MathHelper.clamp_int(coords.getZ(), 202, 823) - 202)/621d*128);
        return new WidgetPosition(x, z);
    }

    @Override
    public void renderWidget(WidgetPosition position) {
        if(Config.crystalHollowsMap > 0 && LocationUtils.onIsland(LocationUtils.Island.CRYSTAL_HOLLOWS) && Minecraft.getMinecraft().thePlayer != null) {
            RenderUtils.renderTexture(maps[Config.crystalHollowsMap-1], position, getWidth(), getHeight());
            WidgetPosition markerPosition = blockPosToMapWidgetPosition(position, Minecraft.getMinecraft().thePlayer.getPosition());
            RenderUtils.renderTexture(marker, markerPosition, 6, 6);
        }
    }

    @Override
    public void renderPreview(WidgetPosition position) {
        RenderUtils.renderTexture(maps[Config.crystalHollowsMap-1], position, getWidth(), getHeight());
    }

    @Override
    public int getWidth() {
        return 128;
    }

    @Override
    public int getHeight() {
        return 128;
    }

    @Override
    public boolean isEnabled() {
        return Config.crystalHollowsMap > 0;
    }

}
