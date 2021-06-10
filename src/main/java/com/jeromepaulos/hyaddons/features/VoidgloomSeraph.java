package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.RenderUtils;
import com.jeromepaulos.hyaddons.utils.ScoreboardUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.Base64;
import java.util.Collection;

public class VoidgloomSeraph {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean activateFeatures = false;

    private static BlockPos beacon = null;

    private void setBeacon(BlockPos pos) {
        beacon = pos;
    }

    private void findBeacon(int radius) {
        if(mc.theWorld != null) {
            new Thread(() -> {
                BlockPos foundBeacon = null;
                for(int x = radius*-1; x <= radius; x++) {
                    for(int y = radius*-1; y <= radius; y++) {
                        for(int z = radius*-1; z <= radius; z++) {

                            double coordsX = x + mc.thePlayer.posX;
                            double coordsY = y + mc.thePlayer.posY;
                            double coordsZ = z + mc.thePlayer.posZ;

                            BlockPos position = new BlockPos(coordsX, coordsY, coordsZ);
                            Block block = mc.theWorld.getBlockState(position).getBlock();

                            if(block == Blocks.beacon) {
                                foundBeacon = position;
                            }
                        }
                    }
                }
                setBeacon(foundBeacon);
            }, "HyAddons-Beacon-Scanner").start();
        }
    }

    private int ticks = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Utils.inSkyBlock) {
            boolean inSepulture = ScoreboardUtils.scoreboardContains("Void Sepulture");
            boolean questActive = ScoreboardUtils.scoreboardContains("Voidgloom Seraph");
            if(Config.ignoreOtherVoidgloom) {
                // only active if your boss is alive
                activateFeatures = questActive && inSepulture && ScoreboardUtils.scoreboardContains("Slay the boss!");
            } else {
                // active all the time
                activateFeatures = inSepulture;
            }

            if(ticks % 4 == 0) {
                if(activateFeatures && Config.highlightVoidgloomBeacons) {
                    findBeacon(Config.voidgloomSearchRadius);
                }
                ticks = 0;
            }
            ticks++;

            if(Config.beaconWarningTitle && Config.highlightVoidgloomBeacons && activateFeatures && beacon != null) {
                Utils.displayTitle(EnumChatFormatting.GREEN+"BEACON NEARBY", null, 1);
            }
        } else {
            activateFeatures = false;
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if(Config.highlightVoidgloomBeacons && beacon != null && activateFeatures) {
            RenderUtils.highlightBlock(beacon, new Color(0,255,0, 77), event.partialTicks);
        }

        if(Config.highlightVoidgloomSkulls && activateFeatures) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().expand(Config.voidgloomSearchRadius, Config.voidgloomSearchRadius, Config.voidgloomSearchRadius);
            Collection<EntityArmorStand> entities = mc.theWorld.getEntitiesWithinAABB(EntityArmorStand.class, bb);
            for(EntityArmorStand entity : entities) {
                if(entity.getEquipmentInSlot(4) != null) {
                    ItemStack item = entity.getEquipmentInSlot(4);
                    if(item.getItem() == Items.skull) {
                        NBTTagCompound nbt = item.getTagCompound();
                        if(nbt.hasKey("SkullOwner")) {
                            String texture = nbt.getCompoundTag("SkullOwner").getCompoundTag("Properties").getTagList("textures", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0).getString("Value");
                            texture = new String(Base64.getDecoder().decode(texture));
                            if(texture.contains("eb07594e2df273921a77c101d0bfdfa1115abed5b9b2029eb496ceba9bdbb4b3")) {
                                RenderUtils.highlightBlock(entity.getPosition(), new Color(0, 0, 255, 77), event.partialTicks);
                            }
                        }
                    }
                }
            }
        }
    }

}
