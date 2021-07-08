package com.jeromepaulos.hyaddons.mixins;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.utils.SummonUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import kotlin.collections.CollectionsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Maybe the regex doesn't have to run on every frame..?

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer {
    @Shadow protected abstract NetworkPlayerInfo getPlayerInfo();

    public MixinAbstractClientPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    // Phoenix Skin Check from Skytils (GPL-3.0)
    private static final String phoenixSkinObject = "eyJ0aW1lc3RhbXAiOjE1NzU0NzAyNzE3MTUsInByb2ZpbGVJZCI6ImRlNTcxYTEwMmNiODQ4ODA4ZmU3YzlmNDQ5NmVjZGFkIiwicHJvZmlsZU5hbWUiOiJNSEZfTWluZXNraW4iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM2YTAzODNhNTI3ODAzZDk5YjY2MmFkMThiY2FjNzhjMTE5MjUwZWJiZmIxNDQ3NWI0ZWI0ZDRhNjYyNzk2YjQifX19";

    private Boolean isSummonMob = null;
    private final Pattern regex = Pattern.compile("[A-Za-z0-9_]{1,16}'s (.*) \\d*❤");

    @Inject(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void replaceSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        if(Config.fixSummonSkins && isSummonMob()) {
            ResourceLocation skin = getSkinFromName(getMobName());
            if(skin != null) {
                cir.setReturnValue(skin);
            }
        }
    }

    private boolean isSummonMob() {
        if(isSummonMob != null) return isSummonMob;
        if(!Utils.inSkyBlock) return false;

        try {
            if(this.getName().equals("Lost Adventurer")) {
                Property textures = CollectionsKt.firstOrNull(this.getGameProfile().getProperties().get("textures"));
                if(textures != null) {
                    isSummonMob = phoenixSkinObject.equals(textures.getValue());
                }
            }
        } catch(Exception e) {
            isSummonMob = false;
        }
        if(isSummonMob == null) isSummonMob = false;

        return isSummonMob;
    }

    private String getMobName() {
        String name = "";

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
            Matcher matcher = regex.matcher(nameTag);
            if(matcher.matches()) {
                name = matcher.group(1);
            }
        }

        return name;
    }

    private ResourceLocation getSkinFromName(String name) {
        if(name == null) return null;
        if(!SummonUtils.skins.containsKey(name)) return null;

        SkinManager skinManager = Minecraft.getMinecraft().getSkinManager();
        String url = "https://textures.minecraft.net/texture/"+SummonUtils.skins.get(name);
        MinecraftProfileTexture profileTexture = new MinecraftProfileTexture(url, null);
        return skinManager.loadSkin(profileTexture, MinecraftProfileTexture.Type.SKIN);
    }

}