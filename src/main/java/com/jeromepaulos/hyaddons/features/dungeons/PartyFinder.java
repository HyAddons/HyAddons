package com.jeromepaulos.hyaddons.features.dungeons;

import com.jeromepaulos.hyaddons.utils.Utils;
import com.jeromepaulos.hyaddons.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PartyFinder {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private Set<Slot> excludedParties = new HashSet<Slot>();
    private Set<Slot> includedParties = new HashSet<Slot>();
    private Set<Slot> otherParties = new HashSet<Slot>();

    /**
     * Modified from Cowlection under MIT license
     * https://github.com/cow-mc/Cowlection/blob/master/LICENSE
     */
    private void highlightSlot(Slot slot, GuiChest guiChest, Color color, int z) {
        int guiLeft = (guiChest.width - 176) / 2;
        int inventoryRows = guiChest.inventorySlots.getSlot(0).inventory.getSizeInventory() / 9;
        int ySize = 222 - 108 + inventoryRows * 18;
        int guiTop = (guiChest.height - ySize) / 2;

        int slotX = guiLeft + slot.xDisplayPosition;
        int slotY = guiTop + slot.yDisplayPosition;

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, z);
        Gui.drawRect(slotX, slotY, slotX + 16, slotY + 16, color.getRGB());
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onRenderGuiBackground(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if(Utils.inSkyBlock && Config.enablePartyFinder) {
            if(event.gui instanceof GuiChest) {
                GuiChest guiChest = (GuiChest) event.gui;
                Container inventorySlots = guiChest.inventorySlots;

                IInventory inventory = inventorySlots.getSlot(0).inventory;
                if(inventory.getName().contains("Party Finder")) {
                    excludedParties.clear();
                    includedParties.clear();
                    otherParties.clear();

                    int slotId = 0;
                    for(Slot slot : inventorySlots.inventorySlots) {
                        slotId++;

                        if(slot.getHasStack()) {
                            ItemStack item = slot.getStack();
                            if(item.getDisplayName().contains("'s Party")) {
                                List<String> lore = item.getTooltip(mc.thePlayer, false);
                                if(lore.get(lore.size() - 1).equals("§5§o§eClick to join!")) {
                                    for (String line : lore) {
                                        line = Utils.removeFormatting(line);
                                        if(Config.excludeCarries && line.startsWith("Note:") && line.toLowerCase().contains("carry")) {
                                            excludedParties.add(slot);
                                        } else if(line.startsWith("Dungeon Level Required: ")) {
                                            int partyLevel = Integer.parseInt(line.replace("Dungeon Level Required: ", ""));
                                            if (partyLevel < Config.minimumCatacombsLevel) {
                                                excludedParties.add(slot);
                                            } else {
                                                includedParties.add(slot);
                                            }
                                        } else if(Config.excludeClass != 0) {
                                            switch(Config.excludeClass) {
                                                case 1:
                                                    if( line.contains(": Archer (") ) {
                                                        excludedParties.add(slot);
                                                    }
                                                case 2:
                                                    if( line.contains(": Berserk (") ) {
                                                        excludedParties.add(slot);
                                                    }
                                                case 3:
                                                    if( line.contains(": Healer (") ) {
                                                        excludedParties.add(slot);
                                                    }
                                                case 4:
                                                    if( line.contains(": Mage (") ) {
                                                        excludedParties.add(slot);
                                                    }
                                                case 5:
                                                    if( line.contains(": Tank (") ) {
                                                        excludedParties.add(slot);
                                                    }
                                            }
                                        } else if(Config.minimumCatacombsLevel > 0) {
                                            otherParties.add(slot);
                                        }
                                    }
                                } else {
                                    excludedParties.add(slot);
                                }
                            }
                        }
                    }

                    for(Slot slot : excludedParties) highlightSlot(slot, guiChest, Config.excludedPartyColor, 260);
                    for(Slot slot : otherParties) highlightSlot(slot, guiChest, Config.otherPartyColor, 120);
                    for(Slot slot : includedParties) highlightSlot(slot, guiChest, Config.includedPartyColor, 120);
                }
            }
        }
    }
}
