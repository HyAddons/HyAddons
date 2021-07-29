package com.jeromepaulos.hyaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonUtils {

    private static final Pattern namePattern = Pattern.compile("§r§.([A-Za-z0-9_]{0,16}) §r§f\\(§r§d(Mage|Tank|Berserker|Healer|Archer) ([IVXL0]{1,7})§r§f\\)§r"); // https://regex101.com/r/JEy3XU/1
    private static final Pattern reviveStonePattern = Pattern.compile("§r Revive Stones: §r§c(\\d*)§r"); // https://regex101.com/r/z9I4hv/1
    private static final Pattern puzzlePattern = Pattern.compile("§r (.*): §r§7\\[§r§.§l([✦✖✔])§r§7]§r"); // https://regex101.com/r/yJoPW3/1
    private static final Pattern secretsPattern = Pattern.compile("§r Secrets Found: §r§b(\\d*)§r"); // https://regex101.com/r/qzebEb/1
    private static final Pattern cryptsPattern = Pattern.compile("§r Crypts: §r§6(\\d*)§r"); // https://regex101.com/r/roxSLS/1/

    private static final HashMap<String, Class> classLookup = new HashMap<String, Class>(){{
        for(Class _class : Class.values()) {
            put(_class.getName(), _class);
        }
    }};

    public static DungeonRun dungeonRun = null;

    public enum Floor {
        ENTERANCE("(E)"),
        FLOOR_1("(F1)"),
        FLOOR_2("(F2)"),
        FLOOR_3("(F3)"),
        FLOOR_4("(F4)"),
        FLOOR_5("(F5)"),
        FLOOR_6("(F6)"),
        FLOOR_7("(F7)"),
        MASTER_1("(M1)"),
        MASTER_2("(M2)"),
        MASTER_3("(M3)"),
        MASTER_4("(M4)"),
        MASTER_5("(M5)"),
        MASTER_6("(M6)"),
        MASTER_7("(M7)");

        private String name;

        Floor(String name) { this.name = name; }
        public String getName() { return name; }
    }

    public enum Class {
        MAGE("Mage"),
        ARCHER("Archer"),
        TANK("Tank"),
        BERSERKER("Berserker"),
        HEALER("Healer");

        private String name;

        Class(String tabName) { this.name = tabName; }
        public String getName() { return name; }
    }

    public static class DungeonRun {
        public final HashMap<String, DungeonPlayer> team = new HashMap<>();
        public final ArrayList<DungeonPuzzle> puzzles = new ArrayList<>();
        public int secretsFound = 0;
        public int cryptsFound = 0;
        public int reviveStones = 0;
        public Floor floor = null;
        public boolean inBoss = false;

        public static class DungeonPlayer {
            public String username;
            public Class _class;
            public int classLevel;

            public DungeonPlayer(String username, Class _class, int classLevel) {
                this.username = username;
                this._class = _class;
                this.classLevel = MathHelper.clamp_int(classLevel, 0, 60);
            }
        }

        private static class DungeonPuzzle {
            public enum Status { UNDISCOVERED, DISCOVERED, COMPLETED, FAILED }
            public Status status;
            public String name;

            public DungeonPuzzle(String name, Status status) {
                this.name = name;
                this.status = status;
            }
        }
    }

    private int counter = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(counter % 20 == 0) {
            if(Utils.inDungeon) {
                if(dungeonRun == null) dungeonRun = new DungeonRun();
                dungeonRun.reviveStones = 0;

                if(dungeonRun.floor == null) {
                    String cataLine = ScoreboardUtils.getLineThatContains("The Catacombs");
                    for(Floor floor : Floor.values()) {
                        if(cataLine.contains(floor.getName())) {
                            dungeonRun.floor = floor;
                        }
                    }
                }

                List<String> tabList = TabUtils.getTabList();
                for(int i = 0; i < tabList.size(); i++) {
                    String name = tabList.get(i);

                    if(name.equals("§r         §r§b§lParty §r§f(1)§r")) {
                        for(int j = i; j < i+20; j++) {
                            name = tabList.get(j);

                            if(dungeonRun.team.size() < 5 && name.contains("(") && name.contains(")")) {
                                Matcher nameMatcher = namePattern.matcher(name);
                                if(nameMatcher.matches()) {
                                    dungeonRun.team.put(
                                            nameMatcher.group(1),
                                            new DungeonRun.DungeonPlayer(
                                                    nameMatcher.group(1),
                                                    classLookup.get(nameMatcher.group(2)),
                                                    Utils.romanToInt(nameMatcher.group(3))
                                            )
                                    );
                                }
                            }

                            if(name.contains("Revive Stones:")) {
                                Matcher reviveStoneMatcher = reviveStonePattern.matcher(name);
                                if(reviveStoneMatcher.matches()) {
                                    dungeonRun.reviveStones += Integer.parseInt(reviveStoneMatcher.group(1));
                                }
                            }
                        }
                    } else if(name.startsWith("§r§b§lPuzzles: §r§f(")) {
                        dungeonRun.puzzles.clear();
                        for(int j = i; j < i+5; j++) {
                            name = tabList.get(j);
                            Matcher puzzleMatcher = puzzlePattern.matcher(name);
                            if(puzzleMatcher.find()) {
                                DungeonRun.DungeonPuzzle puzzle = new DungeonRun.DungeonPuzzle(puzzleMatcher.group(1), null);
                                if(puzzleMatcher.group(2).equals("✦")) puzzle.status = DungeonRun.DungeonPuzzle.Status.DISCOVERED;
                                if(puzzleMatcher.group(2).equals("✖")) puzzle.status = DungeonRun.DungeonPuzzle.Status.FAILED;
                                if(puzzleMatcher.group(2).equals("✔")) puzzle.status = DungeonRun.DungeonPuzzle.Status.COMPLETED;
                                if(puzzle.name.contains("?")) puzzle.status = DungeonRun.DungeonPuzzle.Status.UNDISCOVERED;
                                dungeonRun.puzzles.add(puzzle);
                            }
                        }
                    } else if(name.contains("Crypts: ")) {
                        Matcher cryptsMatcher = cryptsPattern.matcher(name);
                        if(cryptsMatcher.matches()) {
                            dungeonRun.cryptsFound = Integer.parseInt(cryptsMatcher.group(1));
                        }
                    } else if(name.contains("Secrets Found: ")) {
                        Matcher secretsMatcher = secretsPattern.matcher(name);
                        if(secretsMatcher.matches()) {
                            dungeonRun.secretsFound = Integer.parseInt(secretsMatcher.group(1));
                        }
                    }
                }

                if(Minecraft.getMinecraft().theWorld != null && dungeonRun != null) {
                    if((ScoreboardUtils.scoreboardContains("30,30") && (dungeonRun.floor == Floor.FLOOR_1 || dungeonRun.floor == Floor.MASTER_1)) ||
                    (ScoreboardUtils.scoreboardContains("30,125") && (dungeonRun.floor == Floor.FLOOR_2 || dungeonRun.floor == Floor.MASTER_2)) ||
                    (ScoreboardUtils.scoreboardContains("30,225") && (dungeonRun.floor == Floor.FLOOR_3 || dungeonRun.floor == Floor.MASTER_3)) ||
                    (ScoreboardUtils.scoreboardContains("- Healthy") && (dungeonRun.floor == Floor.FLOOR_3 || dungeonRun.floor == Floor.MASTER_3)) ||
                    (ScoreboardUtils.scoreboardContains("30,344") && (dungeonRun.floor == Floor.FLOOR_4 || dungeonRun.floor == Floor.MASTER_4)) ||
                    (ScoreboardUtils.scoreboardContains("livid") && (dungeonRun.floor == Floor.FLOOR_5 || dungeonRun.floor == Floor.MASTER_5)) ||
                    (ScoreboardUtils.scoreboardContains("sadan") && (dungeonRun.floor == Floor.FLOOR_6 || dungeonRun.floor == Floor.MASTER_6)) ||
                    (ScoreboardUtils.scoreboardContains("necron") && (dungeonRun.floor == Floor.FLOOR_7 || dungeonRun.floor == Floor.MASTER_7))) {
                        dungeonRun.inBoss = true;
                    }
                }
            } else {
                dungeonRun = null;
            }

            counter = 0;
        }
        counter++;
    }

    public static void debug() {
        Utils.sendDebugMessage("Floor: "+dungeonRun.floor.name());
        for(DungeonRun.DungeonPlayer player : dungeonRun.team.values()) {
            Utils.sendDebugMessage("Player: "+player.username+" ("+player._class.getName()+" "+player.classLevel+")");
        }
        Utils.sendDebugMessage("Secrets Found: "+dungeonRun.secretsFound);
        Utils.sendDebugMessage("Crypts Found: "+dungeonRun.cryptsFound);
        Utils.sendDebugMessage("Revive Stones: "+dungeonRun.reviveStones);
        for(DungeonRun.DungeonPuzzle puzzle : dungeonRun.puzzles) {
            Utils.sendDebugMessage("Puzzle: "+puzzle.name+" (Status: "+puzzle.status.name()+")");
        }
    }

    public static boolean inFloor(Floor floor) {
        return dungeonRun != null && dungeonRun.floor != null && dungeonRun.floor.equals(floor);
    }

    public static boolean onFloorWithMimic() {
        return dungeonRun != null && dungeonRun.floor != null &&
                dungeonRun.floor != Floor.ENTERANCE &&
                dungeonRun.floor != Floor.FLOOR_1 &&
                dungeonRun.floor != Floor.FLOOR_2 &&
                dungeonRun.floor != Floor.FLOOR_3 &&
                dungeonRun.floor != Floor.MASTER_1 &&
                dungeonRun.floor != Floor.MASTER_2 &&
                dungeonRun.floor != Floor.MASTER_3;
    }

}