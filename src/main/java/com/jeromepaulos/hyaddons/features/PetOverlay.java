package com.jeromepaulos.hyaddons.features;

import com.google.gson.Gson;
import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetOverlay {

    private static final Minecraft mc = Minecraft.getMinecraft();

    Pattern tooltipExpRegex = Pattern.compile("-------------------- ([0-9.,]*)/.*");
    Pattern expBoostSkillRegex = Pattern.compile("Held Item: (.*) Exp.*Boost");
    Pattern petActionRegex = Pattern.compile("You (summoned|despawned) your (.*)!");
    Pattern petLevelUpRegex = Pattern.compile("Your (.*) levelled up to level (\\d{1,3})!");
    // Pattern expGainRegex = Pattern.compile(".*\\+([0-9,.]*) (Farming|Mining|Combat|Foraging|Fishing|Enchanting|Alchemy) \\(([0-9,.]*)/.*");
    Pattern autoPetRegex = Pattern.compile("§cAutopet §eequipped your §7\\[Lvl (\\d{1,3})\\] (.*)§e! §a§lVIEW RULE§r");

    HashMap<String, Float> expLog = new HashMap<String, Float>() {{
        put("Farming", null);
        put("Mining", null);
        put("Combat", null);
        put("Foraging", null);
        put("Fishing", null);
        put("Enchanting", null);
        put("Alchemy", null);
    }};

    HashMap<String, int[]> petExpTable = new HashMap<String, int[]>() {{
        put("common", new int[]{0,100,110,120,130,145,160,175,190,210,230,250,275,300,330,360,400,440,490,540,600,660,730,800,880,960,1050,1150,1260,1380,1510,1650,1800,1960,2130,2310,2500,2700,2920,3160,3420,3700,4000,4350,4750,5200,5700,6300,7000,7800,8700,9700,10800,12000,13300,14700,16200,17800,19500,21300,23200,25200,27400,29800,32400,35200,38200,41400,44800,48400,52200,56200,60400,64800,69400,74200,79200,84700,90700,97200,104200,111700,119700,128200,137200,146700,156700,167700,179700,192700,206700,221700,237700,254700,272700,291700,311700,333700,357700,383700});
        put("uncommon", new int[]{0,175,190,210,230,250,275,300,330,360,400,440,490,540,600,660,730,800,880,960,1050,1150,1260,1380,1510,1650,1800,1960,2130,2310,2500,2700,2920,3160,3420,3700,4000,4350,4750,5200,5700,6300,7000,7800,8700,9700,10800,12000,13300,14700,16200,17800,19500,21300,23200,25200,27400,29800,32400,35200,38200,41400,44800,48400,52200,56200,60400,64800,69400,74200,79200,84700,90700,97200,104200,111700,119700,128200,137200,146700,156700,167700,179700,192700,206700,221700,237700,254700,272700,291700,311700,333700,357700,383700,411700,441700,476700,516700,561700,611700});
        put("rare", new int[]{0,275,300,330,360,400,440,490,540,600,660,730,800,880,960,1050,1150,1260,1380,1510,1650,1800,1960,2130,2310,2500,2700,2920,3160,3420,3700,4000,4350,4750,5200,5700,6300,7000,7800,8700,9700,10800,12000,13300,14700,16200,17800,19500,21300,23200,25200,27400,29800,32400,35200,38200,41400,44800,48400,52200,56200,60400,64800,69400,74200,79200,84700,90700,97200,104200,111700,119700,128200,137200,146700,156700,167700,179700,192700,206700,221700,237700,254700,272700,291700,311700,333700,357700,383700,411700,441700,476700,516700,561700,611700,666700,726700,791700,861700,936700});
        put("epic", new int[]{0,440,490,540,600,660,730,800,880,960,1050,1150,1260,1380,1510,1650,1800,1960,2130,2310,2500,2700,2920,3160,3420,3700,4000,4350,4750,5200,5700,6300,7000,7800,8700,9700,10800,12000,13300,14700,16200,17800,19500,21300,23200,25200,27400,29800,32400,35200,38200,41400,44800,48400,52200,56200,60400,64800,69400,74200,79200,84700,90700,97200,104200,111700,119700,128200,137200,146700,156700,167700,179700,192700,206700,221700,237700,254700,272700,291700,311700,333700,357700,383700,411700,441700,476700,516700,561700,611700,666700,726700,791700,861700,936700,1016700,1101700,1191700,1286700,1386700});
        put("legendary", new int[]{0,660,730,800,880,960,1050,1150,1260,1380,1510,1650,1800,1960,2130,2310,2500,2700,2920,3160,3420,3700,4000,4350,4750,5200,5700,6300,7000,7800,8700,9700,10800,12000,13300,14700,16200,17800,19500,21300,23200,25200,27400,29800,32400,35200,38200,41400,44800,48400,52200,56200,60400,64800,69400,74200,79200,84700,90700,97200,104200,111700,119700,128200,137200,146700,156700,167700,179700,192700,206700,221700,237700,254700,272700,291700,311700,333700,357700,383700,411700,441700,476700,516700,561700,611700,666700,726700,791700,861700,936700,1016700,1101700,1191700,1286700,1386700,1496700,1616700,1746700,1886700});
    }};

    public static Pet currentPet = null;
    public static Pet hoveredPet = null;

    private static class Pet {
        String name;
        Integer level;
        Float exp;
        String rarity;
        String type;
        String expBoostSkill;
        Float expBoostAmount;
        String source;

        public Pet(String name, Integer level, Float exp, String rarity, String type, String expBoostSkill, Float expBoostAmount, String source) {
            this.name = name;
            this.level = level;
            this.exp = exp;
            this.rarity = rarity;
            this.type = type;
            this.expBoostSkill = expBoostSkill;
            this.expBoostAmount = expBoostAmount;
            this.source = source;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    private void savePet() {
        if(currentPet != null) {
            Config.currentPet = currentPet.toString();
        } else {
            Config.currentPet = "";
        }
        HyAddons.config.markDirty();
        HyAddons.config.writeData();
    }

    public static void loadPet() {
        if(!Config.currentPet.equals("")) {
            currentPet = new Gson().fromJson(Config.currentPet, Pet.class);
        } else {
            currentPet = null;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Config.petOverlay && Config.tamingLevel == 0 && mc.thePlayer != null) {
            List<Slot> inventory = mc.thePlayer.openContainer.inventorySlots;
            String inventoryName = inventory.get(0).inventory.getName();
            if(inventoryName.equals("Your Skills")) {
                for(Slot slot : inventory) {
                    if(slot.getHasStack() && slot.getStack().getDisplayName() != null) {
                        String itemName = Utils.removeFormatting(slot.getStack().getDisplayName());
                        if(itemName.contains("Taming")) {
                            try {
                                Config.tamingLevel = Integer.parseInt(itemName.replace("Taming ", ""));
                                HyAddons.config.markDirty();
                                HyAddons.config.writeData();
                            } catch(Exception ignored) {}
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if(event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            if(Config.petOverlay && Utils.inSkyBlock) {
                String petName = "No pet equipped!";

                if(Config.tamingLevel == 0) {
                    petName = "Type /hyaddons to add taming level";
                } else {
                    if(currentPet != null) {
                        petName = EnumChatFormatting.GRAY+"[Lvl "+currentPet.level+"] "+currentPet.name;

                        /*if(currentPet.level == 100) {
                            petName = EnumChatFormatting.GRAY+"[Lvl "+currentPet.level+"] "+currentPet.name;
                        } else if(currentPet.source.equals("autopet")) {
                            petName = EnumChatFormatting.GRAY+"[Lvl "+currentPet.level+"] "+currentPet.name+EnumChatFormatting.DARK_GRAY+" (auto)";
                        } else {
                            int levelExp = petExpTable.get(currentPet.rarity)[currentPet.level];
                            String currentPetPercent = Math.round((currentPet.exp/levelExp*100F)*100F)/100F + "%";
                            petName = EnumChatFormatting.GRAY+"[Lvl "+currentPet.level+"] "+currentPet.name+" "+EnumChatFormatting.WHITE+currentPetPercent;
                        }*/
                    }
                }

                mc.fontRendererObj.drawStringWithShadow(petName, 5, 5, Color.WHITE.getRGB());
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTooltip(ItemTooltipEvent event) {
        if(Utils.inSkyBlock) {
            String inventoryName = mc.thePlayer.openContainer.inventorySlots.get(0).inventory.getName();

            if(inventoryName.equals("Pets") || inventoryName.endsWith(") Pets")) {
                List<String> lore = event.toolTip;
                if(Utils.listContainsString(lore, "Click to summon")) {
                    ItemStack item = event.itemStack;
                    String name = item.getDisplayName();

                    // Find pet's current EXP
                    Float exp = null;
                    Float expBoostAmount = null;
                    String expBoostSkill = null;
                    for(int i = 0; i < lore.size(); i++) {
                        String line = lore.get(i);
                        line = Utils.removeFormatting(line);

                        if(line.equals("MAX LEVEL")) {
                            exp = null;
                        } else {
                            Matcher matcher = tooltipExpRegex.matcher(line);
                            if(matcher.matches()) {
                                exp = Float.parseFloat(matcher.group(1).replace(",", ""));
                            }
                        }

                        Matcher matcher = expBoostSkillRegex.matcher(line);
                        if(matcher.matches()) {
                            expBoostSkill = matcher.group(1).trim();
                            expBoostAmount = 1 + Integer.parseInt(Utils.removeFormatting(lore.get(i + 1)).replaceAll("[^0-9]", "")) / 100F; // 50 becomes 1.5
                        }
                    }

                    // Find pet rarity by color code
                    String rarity = null;
                    if (name.contains("§f")) {
                        rarity = "common";
                    } else if (name.contains("§a")) {
                        rarity = "uncommon";
                    } else if (name.contains("§9")) {
                        rarity = "rare";
                    } else if (name.contains("§5")) {
                        rarity = "epic";
                    } else if (name.contains("§6") || name.contains("§d")) {
                        rarity = "legendary"; // includes both legendary and mythic, they require the same xp
                    }

                    // Pet level
                    int level = Integer.parseInt(Utils.removeFormatting(name).replaceAll("[^0-9]", ""));

                    // Exp type
                    String type = null;
                    if (lore.get(1).contains("Farming")) {
                        type = "Farming";
                    } else if (lore.get(1).contains("Mining")) {
                        type = "Mining";
                    } else if (lore.get(1).contains("Combat")) {
                        type = "Combat";
                    } else if (lore.get(1).contains("Foraging")) {
                        type = "Foraging";
                    } else if (lore.get(1).contains("Fishing")) {
                        type = "Fishing";
                    } else if (lore.get(1).contains("Enchanting")) {
                        type = "Enchanting";
                    } else if (lore.get(1).contains("Alchemy")) {
                        type = "Alchemy";
                    }

                    hoveredPet = new Pet(
                            name.replaceAll("^.*] ", ""),
                            level,
                            exp,
                            rarity,
                            type,
                            expBoostSkill,
                            expBoostAmount,
                            "equip"
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public void onChatEvent(ClientChatReceivedEvent event) {
        if(Utils.inSkyBlock && event != null && event.message != null) {
            String message = event.message.getUnformattedText();

            if(event.type == 0) {
                Matcher petActionMatcher = petActionRegex.matcher(message);
                Matcher petLevelUpMatcher = petLevelUpRegex.matcher(message);
                Matcher autoPetMatcher = autoPetRegex.matcher(event.message.getFormattedText());

                if(petActionMatcher.matches()) {
                    if(petActionMatcher.group(1).equals("summoned")) {
                        if(hoveredPet != null) {
                            currentPet = hoveredPet;
                            savePet();
                        }
                    } else if(petActionMatcher.group(1).equals("despawned")) {
                        currentPet = null;
                        savePet();
                    }
                } else if(petLevelUpMatcher.matches()) {
                    if(currentPet.name.contains(petLevelUpMatcher.group(1))) {
                        currentPet.level = Integer.parseInt(petLevelUpMatcher.group(2));
                        currentPet.exp = 0F;
                    }
                } else if(autoPetMatcher.matches()) {
                    currentPet = new Pet(
                            autoPetMatcher.group(2),
                            Integer.parseInt(autoPetMatcher.group(1)),
                            null,
                            null,
                            null,
                            null,
                            null,
                            "autopet"
                    );
                    savePet();
                }
            } /*else if(event.type == 2) {
                if(currentPet != null && !currentPet.source.equals("autopet")) {
                    Matcher expGainMatcher = expGainRegex.matcher(message);
                    if(expGainMatcher.matches()) {
                        String type = expGainMatcher.group(2);
                        Float currentAmount = Float.parseFloat(expGainMatcher.group(3).replace(",", ""));
                        Float amountGained = Float.parseFloat(expGainMatcher.group(1).replace(",", ""));

                        if(expLog.get(type) == null) {
                            expLog.replace(type, currentAmount-amountGained);
                        }

                        if(!expLog.get(type).equals(currentAmount)) {
                            if(currentPet.type.equals(type)) { // full xp
                                if(currentPet.expBoostSkill.equals(type) || currentPet.expBoostSkill.equals("All Skills")) { // boosted xp
                                    currentPet.exp += (currentAmount - expLog.get(type)) * (1 + Config.tamingLevel/100F) * currentPet.expBoostAmount;
                                } else { // unboosted xp
                                    currentPet.exp += (currentAmount - expLog.get(type)) * (1 + Config.tamingLevel/100F);
                                }
                            } else if(type.equals("Alchemy") || type.equals("Enchanting")) { // 1/12 xp
                                if(currentPet.expBoostSkill.equals(type) || currentPet.expBoostSkill.equals("All Skills")) { // boosted xp
                                    currentPet.exp += (currentAmount - expLog.get(type))/12 * (1 + Config.tamingLevel/100F) * currentPet.expBoostAmount;
                                } else { // unboosted xp
                                    currentPet.exp += (currentAmount - expLog.get(type))/12 * (1 + Config.tamingLevel/100F);
                                }
                            } else { // 1/4 xp
                                if(currentPet.expBoostSkill.equals(type) || currentPet.expBoostSkill.equals("All Skills")) { // boosted xp
                                    currentPet.exp += (currentAmount - expLog.get(type))/4 * (1 + Config.tamingLevel/100F) * currentPet.expBoostAmount;
                                } else { // unboosted xp
                                    currentPet.exp += (currentAmount - expLog.get(type))/4 * (1 + Config.tamingLevel/100F);
                                }
                            }

                            expLog.replace(type, currentAmount);
                            savePet();
                        }
                    }
                }
            }*/
        }

    }

}
