package com.jeromepaulos.hyaddons.features.pets;

import com.google.gson.Gson;
import com.jeromepaulos.hyaddons.HyAddons;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.gui.GuiWidget;
import com.jeromepaulos.hyaddons.gui.WidgetPosition;
import com.jeromepaulos.hyaddons.utils.RenderUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetOverlay extends GuiWidget {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Pattern petNameRegex = Pattern.compile("§7\\[Lvl (\\d{1,3})\\] §([fa956d])(.*)");
    private static final Pattern petActionRegex = Pattern.compile("You (summoned|despawned) your (.*)!");
    private static final Pattern petLevelUpRegex = Pattern.compile("Your (.*) levelled up to level (\\d{1,3})!");
    private static final Pattern autoPetRegex = Pattern.compile("§cAutopet §eequipped your §7\\[Lvl (\\d{1,3})\\] §([fa956d])(.*)§e! §a§lVIEW RULE§r");

    private static class Pet {
        public String name;
        public int level;
        public char color;

        public Pet(String name, int level, char color) {
            this.name = name;
            this.level = level;
            this.color = color;
        }

        public String getFinalName() {
            return "§7[Lvl "+level+"] §"+color+name;
        }

        public String toString() {
            return new Gson().toJson(this);
        }
    }

    private static Pet currentPet = null;
    private static Pet hoveredPet = null;

    public PetOverlay() {
        super("Pet Overlay", new WidgetPosition(5, 5));
        HyAddons.WIDGET_MANAGER.addWidget(this);
    }

    @Override
    public void renderWidget(WidgetPosition position) {
        if(Utils.inSkyBlock && Config.petOverlay) {
            if(currentPet != null) {
                RenderUtils.drawString(currentPet.getFinalName(), position);
            } else {
                RenderUtils.drawString("No pet equipped!", position);
            }
        }
    }

    @Override
    public void renderPreview(WidgetPosition position) {
        RenderUtils.drawString("§7[Lvl 100] §6Ender Dragon", position);
    }

    @Override
    public int getWidth() {
        return RenderUtils.getStringWidth("§7[Lvl 100] §6Ender Dragon");
    }

    @Override
    public int getHeight() {
        return RenderUtils.getLineHeight();
    }

    @Override
    public boolean isEnabled() {
        return Config.petOverlay;
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if(Utils.inSkyBlock && Config.petOverlay) {
            String inventoryName = Utils.getInventoryName();
            if(inventoryName.equals("Pets") || inventoryName.endsWith(") Pets")) {
                if(Utils.listContainsString(event.toolTip, "Click to summon")) {
                    String itemName = event.itemStack.getDisplayName();
                    Matcher petNameMatcher = petNameRegex.matcher(itemName);
                    if(petNameMatcher.matches()) {
                        hoveredPet = new Pet(
                                petNameMatcher.group(3),
                                Integer.parseInt(petNameMatcher.group(1)),
                                petNameMatcher.group(2).charAt(0)
                        );
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if(event.type == 0 && Utils.inSkyBlock && Config.petOverlay) {
            String message = Utils.removeFormatting(event.message.getUnformattedText());

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
                }
            } else if(autoPetMatcher.matches()) {
                currentPet = new Pet(
                        autoPetMatcher.group(3),
                        Integer.parseInt(autoPetMatcher.group(1)),
                        autoPetMatcher.group(2).charAt(0)
                );
                savePet();
            }
        }
    }

    public void savePet() {
        Config.currentPet = currentPet == null ? "" : currentPet.toString();
        HyAddons.config.markDirty();
        HyAddons.config.writeData();
    }

    public static void loadPet() {
        currentPet = Config.currentPet.equals("") ? null : new Gson().fromJson(Config.currentPet, Pet.class);
    }

}
