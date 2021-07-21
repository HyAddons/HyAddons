package com.jeromepaulos.hyaddons.utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

public class LocationUtils {

    private static String prevIsland = null;
    private static Island island = null;
    public static HashMap<String, Island> islandLookup = new HashMap<String, Island>(){{
        for(Island island : Island.values()) {
            put(island.getName(), island);
        }
    }};

    public enum Island {
        PRIVATE_ISLAND("Private World"),
        HUB("Hub"),
        SPIDERS_DEN("Spider's Den"),
        BLAZING_FORTRESS("Blazing Fortress"),
        THE_END("The End"),
        THE_PARK("The Park"),
        GOLD_MINE("Gold Mine"),
        DEEP_CAVERNS("Deep Caverns"),
        DWARVEN_MINES("Dwarven Mines"),
        FARMING_ISLANDS("The Farming Islands"),
        DUNGEON_HUB("Dungeon Hub"),
        CRYSTAL_HOLLOWS("Crystal Hollows"),
        JERRYS_WORKSHOP("Jerry's Workshop");

        private String tabName;

        Island(String tabName) { this.tabName = tabName; }
        public String getName() { return tabName; }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Utils.inSkyBlock) {
            for(String name : TabUtils.getTabList()) {
                if(!name.equals(prevIsland) && name.contains("Area:")) {
                    prevIsland = name;
                    name = Utils.removeFormatting(name).replace("Area: ", "");
                    island = islandLookup.get(name);
                }
            }
        } else {
            island = null;
        }
    }

    public static Island getIsland() {
        return island;
    }

    public static boolean onIsland(Island island) {
        return getIsland() != null && getIsland().equals(island);
    }

}
