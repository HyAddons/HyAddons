package com.jeromepaulos.hyaddons.config;

import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.gui.MoveWidgetGui;
import com.jeromepaulos.hyaddons.utils.Utils;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
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
            type = PropertyType.SWITCH,
            name = "Enable Colored Names",
            description = "Show your own and other colored names",
            category = "General",
            subcategory = "Configuration"
    )
    public static boolean coloredNames = true;
    @Property(
            type = PropertyType.BUTTON,
            name = "Edit GUI Locations",
            description = "Change the position of enabled widgets on your screen",
            category = "General",
            subcategory = "Configuration",
            placeholder = "Edit"
    )
    public static void openGuiEditor() {
        HyAddons.guiToOpen = new MoveWidgetGui();
    }
    @Property(
            type = PropertyType.SELECTOR,
            name = "Check for Updates",
            description = "How should HyAddons check for updates?",
            options = {"Disabled", "Latest", "Stable"},
            category = "General",
            subcategory = "Extra"
    )
    public static int updateType = 1;
    @Property(
            type = PropertyType.SWITCH,
            name = "Show Debug Messages",
            description = "Only turn this off if they are flooding your chat",
            category = "General",
            subcategory = "Extra"
    )
    public static boolean showDebugMessages = true;

    // Mining
    @Property(
            type = PropertyType.SELECTOR,
            name = "Crystal Hollows Map",
            description = "Show a minimap with your approximate location",
            options = {"Disabled", "Enabled", "No Labels"},
            category = "Mining",
            subcategory = "Crystal Hollows"
    )
    public static int crystalHollowsMap = 0;
    /*@Property(
            type = PropertyType.SWITCH,
            name = "Crystal Hollows Waypoints",
            description = "Type [COMMAND] to manage waypoints",
            category = "Mining",
            subcategory = "Crystal Hollows"
    )*/
    public static boolean crystalHollowsWaypoints = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Coordinate Display",
            description = "A simple coordinate widget, available everywhere in Minecraft",
            category = "Mining",
            subcategory = "Crystal Hollows"
    )
    public static boolean coordinateDisplay = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Commission Widget",
            description = "Display a GUI widget with your current commissions",
            category = "Mining",
            subcategory = "Mining"
    )
    public static boolean commissionWidget = false;

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
            name = "Necron Phase Announcements",
            description = "Display a DSM-like title on screen for each phase",
            category = "Dungeons",
            subcategory = "Floor 7"
    )
    public static boolean necronPhaseAnnouncements = false;
    @Property(
            type = PropertyType.SELECTOR,
            name = "Send Mimic Death Message",
            description = "Detect and announce in party chat when the mimic is killed",
            category = "Dungeons",
            subcategory = "Miscellaneous",
            options = {"Disabled", "Mimic killed!", "Mimic exorcised!", "Mimic demolished!", "Mimic vaporized!", "Mimic banished!", "Child destroyed!", "Mimic obliterated!"}
    )
    public static int mimicDeathMessage = 0;
    @Property(
            type = PropertyType.SWITCH,
            name = "Tank Protection Vignette",
            description = "Add a white vignette when you are under the protection of a tank",
            category = "Dungeons",
            subcategory = "Miscellaneous"
    )
    public static boolean tankVignette = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Boss Entry Warning",
            description = "Warns if you are about to enter boss with an incomplete dungeon",
            category = "Dungeons",
            subcategory = "Miscellaneous"
    )
    public static boolean bossEntryWarning = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Bonzo/Spirit Mask Alert",
            description = "Display a large title when either item triggers",
            category = "Dungeons",
            subcategory = "Cooldowns"
    )
    public static boolean maskAlert = false;

    // Pets
    @Property(
            type = PropertyType.SWITCH,
            name = "Show Prehistoric Egg Steps",
            description = "Display the number of steps recorded on the prehistoric egg item",
            category = "Pets"
    )
    public static boolean prehistoricEggSteps = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "Pet Overlay",
            description = "Shows information about your current pet on screen",
            category = "Pets"
    )
    public static boolean petOverlay = false;
    @Property(
            type = PropertyType.TEXT,
            name = "Current Pet",
            category = "Pets",
            hidden = true
    ) // Hidden field used to store currently active pet
    public static String currentPet = "";

    // Miscellaneous
    /*@Property(
            type = PropertyType.SWITCH,
            name = "Show Cake Soul Owner",
            description = "Shows the owner of the cake a soul was obtained from",
            category = "Miscellaneous",
            subcategory = "Tools"
    )*/
    public static boolean showCakeSoulOwner = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Short Party Transfer Command",
            description = "Adds the /pt <ign> command, overrides the alias for /playtime",
            category = "Miscellaneous",
            subcategory = "Tools"
    )
    public static boolean shortPartyTransfer = true;
    @Property(
            type = PropertyType.SWITCH,
            name = "Fix Necromancy Summon Skins",
            description = "Replace the default phoenix skin with the mob's original skin",
            category = "Miscellaneous"
    )
    public static boolean fixSummonSkins = true;

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
            name = "Bridge Message Separator",
            description = "The symbol your bridge uses to separate messages",
            options = {"Username: Message", "Username > Message"},
            category = "Chat Bridge"
    )
    public static int chatBridgeSeparator = 0;
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
            options = {"[DISCORD]", "[DISC]", "[D]", "[BRIDGE]", "[BOT]", "Discord >"},
            category = "Chat Bridge",
            subcategory = "Appearance"
    )
    public static int bridgePrefix = 0;

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

    // Slayers > Voidgloom Seraph
    @Property(
            type = PropertyType.SWITCH,
            name = "Highlight Beacons",
            description = "Highlight nearby beacons placed by a Voidgloom Seraph",
            category = "Slayers",
            subcategory = "Voidgloom Seraph"
    )
    public static boolean highlightVoidgloomBeacons = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Show Path to Beacon",
            description = "Renders a 'chemtrail' behind thrown beacons",
            category = "Slayers",
            subcategory = "Voidgloom Seraph"
    )
    public static boolean showBeaconPath = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Beacon Warning",
            description = "Show a title when there is a beacon near you",
            category = "Slayers",
            subcategory = "Voidgloom Seraph"
    )
    public static boolean beaconWarningTitle = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Wither Shield Cooldown",
            description = "Display a cooldown timer for your wither shield under the crosshair",
            category = "Slayers",
            subcategory = "Voidgloom Seraph"
    )
    public static boolean witherShieldCooldown = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Highlight Nukekubi Fixation Heads",
            description = "Highlight floating heads summoned by a Voidgloom Seraph",
            category = "Slayers",
            subcategory = "Voidgloom Seraph"
    )
    public static boolean highlightVoidgloomSkulls = false;
    @Property(
            type = PropertyType.SWITCH,
            name = "Ignore Other Bosses",
            description = "Only activate features when &oyour&r boss is alive",
            category = "Slayers",
            subcategory = "Voidgloom Seraph"
    )
    public static boolean ignoreOtherVoidgloom = true;

    public static class ConfigSorting extends SortingBehavior {
        @NotNull @Override
        public Comparator<Category> getCategoryComparator() {
            return (o1, o2) -> {
                if(o1.getName().equals("General")) {
                    return -1;
                } else if(o2.getName().equals("General")) {
                    return 1;
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }
    }

    public Config() {
        super(new File("./config/hyaddons.toml"), "HyAddons", new JVMAnnotationPropertyCollector(), new ConfigSorting());
        initialize();
    }

}
