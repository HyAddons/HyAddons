package com.jeromepaulos.hyaddons.config;

import club.sk1er.vigilance.Vigilant;
import club.sk1er.vigilance.data.Property;
import club.sk1er.vigilance.data.PropertyType;

import java.io.File;

public class Config extends Vigilant {

    @Property(
            type = PropertyType.SWITCH,
            name = "Enable Party Finder Overlay",
            description = "Filters parties you can't join or that don't meet your requirements",
            category = "Party Finder"
    )
    public static boolean enablePartyFinder = false;

    @Property(
            type = PropertyType.SLIDER,
            name = "Minimum Catacombs Level",
            description = "Filter parties below this requirement",
            category = "Party Finder",
            subcategory = "Filters",
            min = 0,
            max = 50
    )
    public static int minimumCatacombsLevel = 0;

    @Property(
            type = PropertyType.SWITCH,
            name = "Exclude Carries",
            description = "Mark parties that include \"carry\" in their note",
            category = "Party Finder",
            subcategory = "Filters"
    )
    public static boolean excludeCarries = true;

    @Property(
            type = PropertyType.SELECTOR,
            name = "Exclude Class",
            description = "Prevent dupes by marking parties that already have a player of this class",
            category = "Party Finder",
            subcategory = "Filters",
            options = {"Disabled", "Archer", "Berserker", "Healer", "Mage", "Tank"}
    )
    public static int excludeClass = 0;

    public Config() {
        super(new File("./config/hyaddons.toml"));
        initialize();
    }

}
