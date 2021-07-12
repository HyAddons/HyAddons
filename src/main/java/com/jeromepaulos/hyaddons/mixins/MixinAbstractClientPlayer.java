package com.jeromepaulos.hyaddons.mixins;

import com.google.common.collect.Iterables;
import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.SummonUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(value = AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer {
    public MixinAbstractClientPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    private static final String phoenixSkin = "eyJ0aW1lc3RhbXAiOjE1NzU0NzAyNzE3MTUsInByb2ZpbGVJZCI6ImRlNTcxYTEwMmNiODQ4ODA4ZmU3YzlmNDQ5NmVjZGFkIiwicHJvZmlsZU5hbWUiOiJNSEZfTWluZXNraW4iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM2YTAzODNhNTI3ODAzZDk5YjY2MmFkMThiY2FjNzhjMTE5MjUwZWJiZmIxNDQ3NWI0ZWI0ZDRhNjYyNzk2YjQifX19";

    private Boolean isSummonMob = null;
    private ResourceLocation correctSkin = null;

    @Inject(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void replaceSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        if(Utils.inSkyBlock && Config.fixSummonSkins && isSummonMob()) {
            if(correctSkin != null) {
                cir.setReturnValue(correctSkin);
            } else {
                setMobInfo();
            }
        }
    }

    /**
     * Modified from Skytils under GPL 3.0 license
     * https://github.com/Skytils/SkytilsMod/blob/1.x/LICENSE.md
     * @author My-Name-Is-Jeff
     */
    private boolean isSummonMob() {
        if(isSummonMob != null) return isSummonMob;

        try {
            if(this.getName().equals("Lost Adventurer")) {
                Property texture = Iterables.getFirst(this.getGameProfile().getProperties().get("textures"), null);
                if(texture != null) {
                    isSummonMob = texture.getValue().equals(phoenixSkin);
                }
            }
        } catch(Exception error) {
            isSummonMob = false;
        }

        if(isSummonMob == null) isSummonMob = false;
        if(this.isInvisible()) isSummonMob = false; // Exclude Shadow Assassins, which don't have nametags

        return isSummonMob;
    }

    private void setMobInfo() {
        AxisAlignedBB playerBB = this.getEntityBoundingBox().expand(0.2d, 3, 0.2d);
        List<Entity> nearbyArmorStands = this.getEntityWorld().getEntitiesInAABBexcluding(this, playerBB, entity -> {
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
            for(Map.Entry<String, String> skin : SummonUtils.skins.entrySet()) {
                if(nameTag.contains(skin.getKey())) {
                    correctSkin = getSkinFromID(skin.getValue());
                }
            }
        }
    }

    private ResourceLocation getSkinFromID(String id) {
        SkinManager skinManager = Minecraft.getMinecraft().getSkinManager();
        String url = "https://textures.minecraft.net/texture/"+id;
        MinecraftProfileTexture profileTexture = new MinecraftProfileTexture(url, null);
        return skinManager.loadSkin(profileTexture, MinecraftProfileTexture.Type.SKIN);
    }

}
