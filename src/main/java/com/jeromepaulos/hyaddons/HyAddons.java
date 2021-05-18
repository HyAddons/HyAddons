package com.jeromepaulos.hyaddons;

import com.jeromepaulos.hyaddons.config.Config;
import com.jeromepaulos.hyaddons.config.ConfigCommand;
import com.jeromepaulos.hyaddons.features.PartyFinder;
import com.jeromepaulos.hyaddons.utils.ModCoreInstaller;
import com.jeromepaulos.hyaddons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HyAddons.MODID, name = HyAddons.MODNAME, version = HyAddons.VERSION, clientSideOnly = true)
public class HyAddons {
    public static final String MODID = "@MODID@";
    public static final String MODNAME = "@MODNAME@";
    public static final String VERSION = "@VERSION@";

    private final Config config = new Config();
    @Mod.Instance(MODID)
    public static HyAddons INSTANCE;
    public Config getConfig() {
        return config;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ConfigCommand());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PartyFinder());
        MinecraftForge.EVENT_BUS.register(new Utils());
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);
    }
}