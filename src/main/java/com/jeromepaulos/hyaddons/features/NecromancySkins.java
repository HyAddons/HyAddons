package com.jeromepaulos.hyaddons.features;

import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NecromancySkins {

    private static final Pattern regex = Pattern.compile(".*'s (.*) .*");

    public String getNecromancySkin(AbstractClientPlayer player) {
        if(player.getName().equals("Lost Adventurer")) {
            AxisAlignedBB playerBB = player.getEntityBoundingBox().expand(0.2d, 3, 0.2d);
            List<Entity> nearbyArmorStands = player.getEntityWorld().getEntitiesInAABBexcluding(player, playerBB, entity -> {
                // nametag armor stand detection from Cowlection
                if(entity instanceof EntityArmorStand) {
                    EntityArmorStand stand = (EntityArmorStand) entity;
                    if(stand.isInvisible() && stand.hasCustomName()) {
                        for(ItemStack equipment : stand.getInventory()) {
                            if(equipment != null) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                return false;
            });

            if(nearbyArmorStands.size() > 0) {
                String nameTag = Utils.removeFormatting(nearbyArmorStands.get(0).getCustomNameTag());
                Matcher matcher = regex.matcher(nameTag);
                if(matcher.matches()) {
                    return "true";
                }
            }
        }
        return null;
    }

}
