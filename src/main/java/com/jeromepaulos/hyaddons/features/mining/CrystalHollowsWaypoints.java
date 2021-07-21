package com.jeromepaulos.hyaddons.features.mining;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.RenderUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrystalHollowsWaypoints {

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if(Utils.inSkyBlock && Config.crystalHollowsWaypoints) {
            RenderUtils.renderBeaconText("Crystal Nucleus", 513, 106, 513, event.partialTicks);
        }
    }

}
