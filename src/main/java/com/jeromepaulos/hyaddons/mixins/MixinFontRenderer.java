package com.jeromepaulos.hyaddons.mixins;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.features.misc.ColoredNames;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    @ModifyVariable(method = "renderString", at = @At(value = "FIELD"))
    private String replaceName(String original) {
        if(Config.coloredNames) {
            String message = ColoredNames.textCache.get(original);
            if(message != null) {
                return message;
            } else {
                String coloredMessage = ColoredNames.replaceString(original);
                ColoredNames.textCache.put(original, coloredMessage);
                return coloredMessage;
            }
        }
        return original;
    }

}


