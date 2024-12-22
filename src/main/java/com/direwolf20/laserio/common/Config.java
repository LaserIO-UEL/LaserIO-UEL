package com.direwolf20.laserio.common;

import com.direwolf20.laserio.common.items.upgrades.OverclockerCard;
import com.direwolf20.laserio.setup.Registration;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

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
    public static List<? extends Integer> ENERGYTIERS_MAXFE_DEFAULT = List.of();
    public static List<? extends String> ENERGYTIERS_PATH_DEFAULT = List.of();

    //public static ForgeConfigSpec.IntValue BASE_MILLI_BUCKETS_FLUID;
    //public static ForgeConfigSpec.IntValue MULTIPLIER_MILLI_BUCKETS_FLUID;
    public static ForgeConfigSpec.IntValue ENERGYCARD_MAXFE;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> ENERGYTIERS_MAXFE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENERGYTIERS_PATH;

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
        ENERGYCARD_MAXFE = COMMON_BUILDER.comment("Maximum FE/t for Energy Cards")
                .defineInRange("energycard_max_fe", 1000000, 0, Integer.MAX_VALUE);

        ENERGYTIERS_PATH = COMMON_BUILDER.comment("Energy over clock tier path names")
                .comment("Regular path rules apply: ONLY lowercase and no spaces")
                .comment("You may put as many as you wish, example: ['exampleoverclock','iron_overclock','gold_over_clock']")
                .defineListAllowEmpty("energytiers_name", ENERGYTIERS_PATH_DEFAULT, Config::validatePath);

        ENERGYTIERS_MAXFE = COMMON_BUILDER.comment("Energy over clock tier maximum FE/t")
                .comment("The first value in the list is linked to the first path name... etc")
                .comment("You may put as many as you did path names, example: [512,1000,2000]")
                .defineListAllowEmpty("energytiers_max_fe", ENERGYTIERS_MAXFE_DEFAULT, Config::validateFE);
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
    public static boolean validateFE(final Object objj) {
        Boolean bool = true;
        for (int i = 0; i < ENERGYTIERS_MAXFE.get().size(); i++) {if (ENERGYTIERS_MAXFE.get().get(i) < 0) {bool = false; break;}}
        return bool;
    }
    public static boolean validatePath(final Object obj) {
        return true;
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

    public static Config OCRegister(String display, int tier) {
        OverclockerCard material;
        material = new OverclockerCard(tier);
        return null;
    }

    /*
    //Create map and add values
    Item OverclockerCard createCardFeMap(String name, Integer value ) {
        Map<String, Integer> map = new HashMap<>();
        map.put(name, value);
    }
    */

}