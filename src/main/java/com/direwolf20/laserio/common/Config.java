package com.direwolf20.laserio.common;

import com.direwolf20.laserio.common.items.upgrades.OverclockerCard;
import com.direwolf20.laserio.setup.Registration;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = LaserIO.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class Config {
    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

    public static final String CATEGORY_CARD = "card";
    //public static final String SUBCATEGORY_FLUID = "fluid_card";
    public static final String SUBCATEGORY_ENERGY = "energy_card";
    public static ForgeConfigSpec COMMON_CONFIG;
    //public static final String SUBCATEGORY_CHEMICAL = "chemical_card";
    public static List<? extends Integer> OC_FE_DEFAULT = List.of(2048,8192,32768,131072,524288,2097152,8388602,134217728,Integer.MAX_VALUE);

    //public static ForgeConfigSpec.IntValue BASE_MILLI_BUCKETS_FLUID;
    //public static ForgeConfigSpec.IntValue MULTIPLIER_MILLI_BUCKETS_FLUID;
    public static ForgeConfigSpec.IntValue BASE_FE_TICK;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> OC_FE;

    //public static ForgeConfigSpec.IntValue BASE_MILLI_BUCKETS_CHEMICAL;
    //public static ForgeConfigSpec.IntValue MULTIPLIER_MILLI_BUCKETS_CHEMICAL;

    public static void register() {
        //registerClientConfigs();
        registerCommonConfigs();
        //registerServerConfigs();
    }

    private static void registerClientConfigs() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }

    private static void registerCommonConfigs() {
        COMMON_BUILDER.comment("Card settings").push(CATEGORY_CARD);
        cardConfig();
        COMMON_BUILDER.pop();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

    private static void registerServerConfigs() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }

    private static void cardConfig() {
        //For fluid cards (Unused)
        //COMMON_BUILDER.comment("Fluid Card").push(SUBCATEGORY_FLUID);
        //BASE_MILLI_BUCKETS_FLUID = COMMON_BUILDER.comment("Millibuckets for Fluid Cards without Overclockers installed")
        //        .defineInRange("base_milli_buckets_fluid", 5000, 0, Integer.MAX_VALUE);
        //MULTIPLIER_MILLI_BUCKETS_FLUID = COMMON_BUILDER.comment("Multiplier for Overclocker Cards - Number of Overclockers * this value = max millibuckets")
        //        .defineInRange("multiplier_milli_buckets_fluid", 10000, 0, Integer.MAX_VALUE);
        //COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Energy Card").push(SUBCATEGORY_ENERGY);
        BASE_FE_TICK = COMMON_BUILDER.comment("Base Maximum FE/T for Energy Cards")
                .defineInRange("max_fe_tick", 1000000, 0, Integer.MAX_VALUE);

        OC_FE = COMMON_BUILDER.comment("OverClock Values")
                .defineListAllowEmpty("max_fe_octier", OC_FE_DEFAULT, Config::validateNum);
        COMMON_BUILDER.pop();

        //For gas cards (NYI)
        /*COMMON_BUILDER.comment("Chemical Card").push(SUBCATEGORY_CHEMICAL);
        BASE_MILLI_BUCKETS_CHEMICAL = COMMON_BUILDER.comment("Millibuckets for Chemical Cards without Overclockers installed (only if Mekanism is installed)")
                .defineInRange("base_milli_buckets_chemical", 15000, 0, Integer.MAX_VALUE);
        MULTIPLIER_MILLI_BUCKETS_CHEMICAL = COMMON_BUILDER.comment("Multiplier for Overclocker Cards - Number of Overclockers * this value = max millibuckets  (only if Mekanism is installed)")
                .defineInRange("multiplier_milli_buckets_chemical", 60000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
        */
    }

    //If value is less than 0, clear
    public static boolean validateNum(final Object objj) {
        Boolean bool = true;
        for (int i = 0; i < OC_FE.get().size(); i++) {if (OC_FE.get().get(i) < 0) {bool = false; break;}}
        return bool;
    }

    public static void loadConfig(ForgeConfigSpec spec, java.nio.file.Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }

    //Create map and add values
    //Item OverclockerCard createCardFeMap(String name, Integer value ) {
    //    Map<String, Integer> map = new HashMap<>();
    //    map.put(name, value);
    //}


}