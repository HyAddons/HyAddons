package com.jeromepaulos.hyaddons.mixins;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.features.misc.ColoredNames;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    private HashMap<String, String> messageCache = new HashMap<>();

    @ModifyVariable(method = "drawChat", at = @At(value = "STORE"))
    private String replaceColoredNames(String original) {
        if(Config.coloredNames) {
            String message = messageCache.get(original);
            if(message != null) {
                return message;
            } else {
                String coloredMessage = ColoredNames.replaceString(original);
                messageCache.put(original, coloredMessage);
                return coloredMessage;
            }
        }
        return original;
    }

}
