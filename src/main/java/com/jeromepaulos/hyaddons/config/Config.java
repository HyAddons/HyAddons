package com.jeromepaulos.hyaddons.config;

import club.sk1er.vigilance.Vigilant;
import club.sk1er.vigilance.data.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;

public class Config extends Vigilant {

    @Property(
            type = PropertyType.BUTTON,
            name = "&f&lHyAddons &r&7by HY7",
            description = "https://discord.gg/bz3R9hWjD3",
            category = "General",
            placeholder = "Join Discord"
    )
    public static void openDiscord() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://discord.gg/bz3R9hWjD3"));
    }

    // Party Finder
    @Property(
            type = PropertyType.SWITCH,
            name = "Enable Party Finder Overlay",
            description = "Filter parties that you can't join",
            category = "Party Finder"
    )
    public static boolean enablePartyFinder = false;
    @Property(
            type = PropertyType.SLIDER,
            name = "Minimum Catacombs Level",
            description = "Exclude parties below this requirement",
            category = "Party Finder",
            subcategory = "Filters",
            min = 0,
            max = 50
    )
    public static int minimumCatacombsLevel = 0;
    @Property(
            type = PropertyType.SWITCH,
            name = "Exclude Carries",
            description = "Exclude parties that include \"carry\" in their note",
            category = "Party Finder",
            subcategory = "Filters"
    )
    public static boolean excludeCarries = true;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Exclude Class",
            description = "Exclude parties that already have a player of this class",
            category = "Party Finder",
            subcategory = "Filters",
            options = {"Disabled", "Archer", "Berserker", "Healer", "Mage", "Tank"}
    )
    public static int excludeClass = 0;

    // Chat Bridge
    @Property(
            type = PropertyType.SWITCH,
            name = "Enable Chat Bridge",
            description = "Format messages for your guild's Discord bridge",
            category = "Chat Bridge"
    )
    public static boolean enableBridge = false;
    @Property(
            type = PropertyType.TEXT,
            name = "Bridge Username",
            description = "Enter the username of the bridge bot's account",
            category = "Chat Bridge"
    )
    public static String bridgeBotUsername = "";
    @Property(
            type = PropertyType.SELECTOR,
            name = "Bridge Message Color",
            description = "Choose the color of the formatted message",
            options = {"§1Dark Blue", "§2Dark Green", "§3Turquoise", "§4Dark Red", "§5Purple", "§6Gold", "§fWhite",
                       "§7Light Gray", "§9Light Blue", "§aLime Green", "§bCyan", "§cLight Red", "§dPink", "§eYellow"},
            category = "Chat Bridge",
            subcategory = "Appearance"
    )
    public static int bridgeMessageColor = 8;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Bridge Message Prefix",
            description = "Choose a prefix for the formatted message",
            options = {"[DISCORD]", "[DISC]", "[D]", "[BRIDGE]"},
            category = "Chat Bridge",
            subcategory = "Appearance"
    )
    public static int bridgePrefix = 0;

    public static class ConfigSorting extends SortingBehavior {
        @NotNull @Override
        public Comparator<Category> getCategoryComparator() {
            return new Comparator<Category>() {
                @Override
                public int compare(Category o1, Category o2) {
                    if(o1.getName().equals("General")) {
                        return -1;
                    } else if(o2.getName().equals("General")) {
                        return 1;
                    } else {
                        return o1.getName().compareTo(o2.getName());
                    }
                }
            };
        }
    }

    public Config() {
        super(new File("./config/hyaddons.toml"), "HyAddons", new JVMAnnotationPropertyCollector(), new ConfigSorting());
        initialize();
    }

}
