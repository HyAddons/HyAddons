package com.jeromepaulos.hyaddons.mixins;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {

    @Inject(method="getEntityTexture", at=@At("HEAD"), cancellable = true)
    public void getEntityTexture(AbstractClientPlayer player, CallbackInfoReturnable<ResourceLocation> cir) {
        System.out.println("Does this work?");
        cir.setReturnValue( new ResourceLocation("hyaddons:skin.png") );
    }

}
