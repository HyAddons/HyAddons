package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.events.BlockChangeEvent;
import com.jeromepaulos.hyaddons.utils.RenderUtils;
import com.jeromepaulos.hyaddons.utils.ScoreboardUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;

public class VoidgloomSeraph {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean activateFeatures = false;
    private static final Color beaconColor = new Color(0,255,0, 77);

    private static BlockPos beacon = null;
    private static EntityArmorStand beaconEntity = null;
    private static ArrayList<Vec3> beaconPath = new ArrayList<>();
    private static int ticksSincePath = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(Utils.inSkyBlock) {
            if(Config.ignoreOtherVoidgloom) {
                activateFeatures = ScoreboardUtils.scoreboardContains("Voidgloom Seraph") && ScoreboardUtils.scoreboardContains("Slay the boss!");
            } else {
                activateFeatures = true;
            }

            // Check for Beacon Entities | Modified from Skytils (GPL-3.0)
            if(mc.thePlayer != null) {
                AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().expand(20, 20, 20);
                Collection<EntityArmorStand> entities = mc.theWorld.getEntitiesWithinAABB(EntityArmorStand.class, bb);
                for(EntityArmorStand entity : entities) {
                    for(ItemStack item : entity.getInventory()) {
                        if(item != null) {
                            if(item.getItem() == Item.getItemFromBlock(Blocks.beacon)) {
                                beaconEntity = entity;
                            }
                        }
                    }
                }
            }

            if(beaconPath.size() > 0) {
                ticksSincePath++;
            }

            // Record Path of Beacon
            if(Config.showBeaconPath && beaconEntity != null && activateFeatures) {
                Vec3 beaconPosition = beaconEntity.getPositionVector();
                beaconPosition = new Vec3(beaconPosition.xCoord, beaconPosition.yCoord+1, beaconPosition.zCoord);
                beaconPath.add(beaconPosition);
            }

            // Display Beacon Warning
            if(Config.beaconWarningTitle && activateFeatures && beacon != null) {
                Utils.displayTitle(EnumChatFormatting.GREEN+"BEACON NEARBY", null, 1);
            }
        } else {
            activateFeatures = false;
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        try {
            if (Utils.inSkyBlock && activateFeatures && (Config.highlightVoidgloomBeacons || Config.showBeaconPath || Config.beaconWarningTitle)) {
                if (beacon == null && beaconEntity != null && beaconEntity.getPosition().distanceSq(event.position) < 5 && event.newBlock.getBlock() instanceof BlockBeacon) {
                    beaconEntity = null;
                    beacon = event.position;
                } else if (event.oldBlock.getBlock() instanceof BlockBeacon && event.newBlock.getBlock() instanceof BlockAir && beacon.equals(event.position)) {
                    beacon = null;
                }
            }
        } catch(Exception ignored) {}
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if(ticksSincePath < 120) {
            for(int i = 0; i < beaconPath.size(); i++) {
                if(i > 0) {
                    int alpha = ticksSincePath < 100 ? 255 : 255*(120-ticksSincePath)/50;
                    Vec3 pos1 = beaconPath.get(i);
                    Vec3 pos2 = beaconPath.get(i-1);
                    RenderUtils.draw3DLine(pos1, pos2, new Color(0, 255, 0, alpha), 5, false, event.partialTicks);
                }
            }
        } else {
            ticksSincePath = 0;
            beaconPath.clear();
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if(Config.highlightVoidgloomBeacons && beacon != null && activateFeatures) {
            RenderUtils.highlightBlock(beacon, beaconColor, event.partialTicks);
        }

        if(Config.highlightVoidgloomSkulls && activateFeatures) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().expand(20, 20, 20);
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
                                RenderUtils.highlightBlock(entity.getPosition(), beaconColor, event.partialTicks);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        beacon = null;
        beaconPath.clear();
        beaconEntity = null;
        ticksSincePath = 0;
    }

}
