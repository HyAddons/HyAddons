package com.jeromepaulos.hyaddons;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.config.ConfigCommand;
import com.jeromepaulos.hyaddons.features.ChatBridge;
import com.jeromepaulos.hyaddons.features.PartyFinder;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = HyAddons.MODID, name = HyAddons.MODNAME, version = HyAddons.VERSION, clientSideOnly = true)
public class HyAddons {
    public static final String MODID = "hyaddons";
    public static final String MODNAME = "HyAddons";
    public static final String VERSION = "@VERSION@";

    public static Config config;
    public static GuiScreen guiToOpen = null;
    private static final Minecraft mc = Minecraft.getMinecraft();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new PartyFinder());
        MinecraftForge.EVENT_BUS.register(new ChatBridge());
        MinecraftForge.EVENT_BUS.register(new Utils());

        config = new Config();
        config.preload();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) {
            if(guiToOpen != null) {
                mc.displayGuiScreen(guiToOpen);
                guiToOpen = null;
            }
        }
    }
}