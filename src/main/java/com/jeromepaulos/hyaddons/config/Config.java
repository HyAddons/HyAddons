package com.jeromepaulos.hyaddons.config;

import club.sk1er.vigilance.Vigilant;
import club.sk1er.vigilance.data.*;
import com.jeromepaulos.hyaddons.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.Comparator;

public class Config extends Vigilant {

    // General
    @Property(
            type = PropertyType.BUTTON,
            name = "Have an idea? Need support?",
            description = "https://discord.gg/bz3R9hWjD3",
            category = "General",
            placeholder = "Join Discord"
    )
    public static void openDiscord() {
        Utils.openUrl("https://discord.gg/bz3R9hWjD3");
    }
    @Property(
            type = PropertyType.SELECTOR,
            name = "Check for Updates",
            description = "How should HyAddons check for updates?",
            options = {"Disabled", "Latest", "Stable"},
            category = "General"
    )
    public static int updateType = 2;

    // Dungeons
    @Property(
            type = PropertyType.SWITCH,
            name = "Explosive Shot Cooldown",
            description = "Display a cooldown timer for your explosive shot under the crosshair",
            category = "Dungeons",
            subcategory = "Cooldowns"
    )
    public static boolean explosiveShotCooldown = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Seismic Wave Cooldown",
            description = "Display a cooldown timer for your seismic wave under the crosshair",
            category = "Dungeons",
            subcategory = "Cooldowns"
    )
    public static boolean seismicWaveCooldown = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Wither Shield Cooldown",
            description = "Display a cooldown timer for your wither shield under the crosshair",
            category = "Dungeons",
            subcategory = "Cooldowns"
    )
    public static boolean witherShieldCooldown = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Necron Phase Announcements",
            description = "Display a DSM-like title on screen for each phase",
            category = "Dungeons",
            subcategory = "Floor 7"
    )
    public static boolean necronPhaseAnnouncements = false;

    // Miscellaneous
    @Property(
            type = PropertyType.SWITCH,
            name = "Show SkyBlock ID",
            description = "Requires advanced tooltips to be enabled (F3+H)",
            category = "Miscellaneous",
            subcategory = "Tools"
    )
    public static boolean showSkyBlockId = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Show Cake Soul Owner",
            description = "Shows the owner of the cake a soul was obtained from",
            category = "Miscellaneous",
            subcategory = "Tools"
    )
    public static boolean showCakeSoulOwner = false;

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
    @Property(
            type = PropertyType.COLOR,
            name = "Included Party Color",
            description = "Defaults to green",
            category = "Party Finder",
            subcategory = "Appearance"
    )
    public static Color includedPartyColor = new Color(0,170,0);
    @Property(
            type = PropertyType.COLOR,
            name = "Excluded Party Color",
            description = "Defaults to red",
            category = "Party Finder",
            subcategory = "Appearance"
    )
    public static Color excludedPartyColor = new Color(170,0,0);
    @Property(
            type = PropertyType.COLOR,
            name = "Other Party Color",
            description = "Defaults to orange",
            category = "Party Finder",
            subcategory = "Appearance"
    )
    public static Color otherPartyColor = new Color(187, 123, 2);

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
            options = {"[DISCORD]", "[DISC]", "[D]", "[BRIDGE]", "[BOT]"},
            category = "Chat Bridge",
            subcategory = "Appearance"
    )
    public static int bridgePrefix = 0;

    // Pet Overlay
    @Property(
            type = PropertyType.SWITCH,
            name = "Pet Overlay",
            description = "Shows information about your current pet on screen",
            category = "Pet Overlay"
    )
    public static boolean petOverlay = false;
    @Property(
            type = PropertyType.SLIDER,
            name = "Taming Level",
            description = "Open your skills menu to set it automatically",
            category = "Pet Overlay",
            subcategory = "Configuration",
            max = 50
    )
    public static int tamingLevel = 0;
    @Property(
            type = PropertyType.TEXT,
            name = "Current Pet",
            category = "Pet Overlay",
            hidden = true
    ) // Hidden field used to store currently active pet
    public static String currentPet = "";

    // Spam Hider
    @Property(
            type = PropertyType.SWITCH,
            name = "Hide Guild Join/Leave Messages",
            description = "&2Guild > &bHY7 &ejoined.",
            category = "Spam Hider",
            subcategory = "Hypixel"
    )
    public static boolean guildJoinLeaveMessages = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Hide Friend Join/Leave Messages",
            description = "&aFriend > &bHY7 &eleft.",
            category = "Spam Hider",
            subcategory = "Hypixel"
    )
    public static boolean friendJoinLeaveMessages = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Hide Pickaxe Ability Messages",
            description = "&6Mining Speed Boost &ais now available!",
            category = "Spam Hider",
            subcategory = "SkyBlock"
    )
    public static boolean pickaxeAbilityMessages = false;

    // Voidgloom Seraph
    @Property(
            type = PropertyType.SWITCH,
            name = "Highlight Beacons",
            description = "Highlight nearby beacons placed by a Voidgloom Seraph",
            category = "Voidgloom Seraph"
    )
    public static boolean highlightVoidgloomBeacons = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Highlight Nukekubi Fixation Heads",
            description = "Highlight floating heads summoned by a Voidgloom Seraph",
            category = "Voidgloom Seraph"
    )
    public static boolean highlightVoidgloomSkulls = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Ignore Other Bosses",
            description = "Only activate features when &oyour&r boss is alive",
            category = "Voidgloom Seraph"
    )
    public static boolean ignoreOtherVoidgloom = true;
    @Property(
            type = PropertyType.SLIDER,
            name = "Boss Search Radius",
            description = "Set the distance to search for beacons and bosses\nMay degrade performance and usefulness at high values",
            category = "Voidgloom Seraph",
            min = 5,
            max = 40
    )
    public static int voidgloomSearchRadius = 20;

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
