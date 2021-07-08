package com.jeromepaulos.hyaddons;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.config.ConfigCommand;
import com.jeromepaulos.hyaddons.features.*;
import com.jeromepaulos.hyaddons.updates.UpdateGui;
import com.jeromepaulos.hyaddons.updates.Updater;
import com.jeromepaulos.hyaddons.utils.SummonUtils;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
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
    public static String update = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
        ClientCommandHandler.instance.registerCommand(new PartyTransfer());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new PartyFinder());
        MinecraftForge.EVENT_BUS.register(new ChatBridge());
        MinecraftForge.EVENT_BUS.register(new SpamHider());
        MinecraftForge.EVENT_BUS.register(new Utils());
        MinecraftForge.EVENT_BUS.register(new PetOverlay());
        MinecraftForge.EVENT_BUS.register(new DevTools());
        MinecraftForge.EVENT_BUS.register(new CakeSoul());
        MinecraftForge.EVENT_BUS.register(new VoidgloomSeraph());
        MinecraftForge.EVENT_BUS.register(new DungeonCooldowns());
        MinecraftForge.EVENT_BUS.register(new NecronPhases());
        MinecraftForge.EVENT_BUS.register(new MimicDeath());
        MinecraftForge.EVENT_BUS.register(new DungeonWarning());
        MinecraftForge.EVENT_BUS.register(new DungeonVignette());
        MinecraftForge.EVENT_BUS.register(new ColoredNames());
        MinecraftForge.EVENT_BUS.register(new Memes());
        // MinecraftForge.EVENT_BUS.register(new NecromancySkins());

        config = new Config();
        config.preload();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        new Updater();
        ColoredNames.loadNames();
        SummonUtils.loadSkins();
        PetOverlay.loadPet();
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

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if(update != null && event.gui.getClass() == GuiMainMenu.class) {
            guiToOpen = new UpdateGui();
        }
    }

}