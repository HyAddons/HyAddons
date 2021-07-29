package com.jeromepaulos.hyaddons.features.dungeons;

import com.jeromepaulos.hyaddons.utils.ScoreboardUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import com.jeromepaulos.hyaddons.config.Config;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class DungeonWarning {

    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Config.bossEntryWarning && Utils.inDungeon) {
            if(mc.theWorld != null && mc.thePlayer != null) {
                Block block = mc.theWorld.getBlockState(mc.thePlayer.getPosition()).getBlock();
                if(block == Blocks.portal) {
                    if(ScoreboardUtils.scoreboardContains("Dungeon Cleared")) {
                        if(!ScoreboardUtils.scoreboardContains("Dungeon Cleared: 97%") && !ScoreboardUtils.scoreboardContains("Dungeon Cleared: 100%")) {
                            Utils.displayTitle(EnumChatFormatting.RED+"DUNGEON UNCLEARED", null, 1);
                        }
                    }
                }
            }
        }
    }

}
