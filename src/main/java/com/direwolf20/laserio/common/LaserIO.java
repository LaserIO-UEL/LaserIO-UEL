package com.direwolf20.laserio.common;

import com.direwolf20.laserio.setup.ClientSetup;
import com.direwolf20.laserio.setup.Config;
import com.direwolf20.laserio.setup.ModSetup;
import com.direwolf20.laserio.setup.Registration;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LaserIO.MODID)
public class LaserIO {
    public static final String MODID = "laserio";
    public static final String MODNAME = "LaserIO";
    private static final Logger LOGGER = LogUtils.getLogger();

    public LaserIO() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));
        // Register the deferred registry
        Registration.init();
        // Register the setup method for modloading
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the enqueueIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        //MinecraftForge.EVENT_BUS.register(this);
        // Register 'ModSetup::init' to be called at mod setup time (server and client)
        modbus.addListener(ModSetup::init);
        ModSetup.TABS.register(modbus);
        //modbus.addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);
        // Register 'ClientSetup::init' to be called at mod setup time (client only)
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(ClientSetup::init));
    }
}