package com.jeromepaulos.hyaddons.mixins;

import com.jeromepaulos.hyaddons.features.ColoredNames;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @ModifyVariable(method = "drawChat", at = @At(value = "STORE"))
    private String replaceColoredNames(String original) {
        return ColoredNames.replaceString(original);
    }

}
