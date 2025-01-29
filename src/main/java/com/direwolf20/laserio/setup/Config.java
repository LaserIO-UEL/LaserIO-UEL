package com.direwolf20.laserio.setup;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.nio.file.Path;
import java.util.List;

@EventBusSubscriber
public class Config {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_CARD = "card";
    public static final String SUBCATEGORY_ITEM = "item_card";
    public static final String SUBCATEGORY_FLUID = "fluid_card";
    public static final String SUBCATEGORY_ENERGY = "energy_card";
    public static final String SUBCATEGORY_ENERGY_OVERCLOCKERS = "energy_overclockers";
    public static final String SUBCATEGORY_CHEMICAL = "chemical_card";

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.IntValue MAX_INTERACTION_RANGE;
    public static ForgeConfigSpec.IntValue MAX_NODES_DISTANCE;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> MIN_TICKS_ITEM;
    public static ForgeConfigSpec.IntValue BASE_MILLI_BUCKETS_FLUID;
    public static ForgeConfigSpec.IntValue MULTIPLIER_MILLI_BUCKETS_FLUID;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> MIN_TICKS_FLUID;
    public static ForgeConfigSpec.IntValue MAX_FE_NO_TIERS;
    public static ForgeConfigSpec.IntValue MIN_TICKS_ENERGY;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> MAX_FE_TIERS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> NAME_TIERS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> COLOR_TIERS;
    public static ForgeConfigSpec.IntValue BASE_MILLI_BUCKETS_CHEMICAL;
    public static ForgeConfigSpec.IntValue MULTIPLIER_MILLI_BUCKETS_CHEMICAL;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> MIN_TICKS_CHEMICAL;

    //Validation
    private static boolean positiveIntegerValidator(Object obj) {
        if (obj instanceof Integer num) {
            return (num > 0);
        }
        return false;
    }

    private static boolean nameValidator(Object obj) {
        if (obj instanceof String) {
            return true;
        }
        return false;
    }

    private static boolean colorValidator(Object obj) {
        if (obj instanceof String color) {
            try {
                Integer.decode(color);
                return true;
            } catch(NumberFormatException e) {
            }
        }
        return false;
    }

    //Config build
    static {
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        MAX_INTERACTION_RANGE = COMMON_BUILDER.comment("Maximum interaction range with nodes when using a Laser Wrench")
                .defineInRange("max_interaction_range", 10, 1, Integer.MAX_VALUE);
        MAX_NODES_DISTANCE = COMMON_BUILDER.comment("Maximum distance between two linked nodes")
                .defineInRange("max_nodes_distance", 8, 2, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Card settings").push(CATEGORY_CARD);

        COMMON_BUILDER.comment("Item Card").push(SUBCATEGORY_ITEM);
        MIN_TICKS_ITEM = COMMON_BUILDER.comment("Minimum ticks/operation, the first element is with no Logistic Overclockers, the last one is with all four")
                .defineList("min_ticks_item", List.of(20, 16, 12, 8, 4), Config::positiveIntegerValidator);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Fluid Card").push(SUBCATEGORY_FLUID);
        BASE_MILLI_BUCKETS_FLUID = COMMON_BUILDER.comment("Millibuckets for Fluid Cards without Overclockers installed")
                .defineInRange("base_milli_buckets_fluid", 5000, 0, Integer.MAX_VALUE);
        MULTIPLIER_MILLI_BUCKETS_FLUID = COMMON_BUILDER.comment("Multiplier for Overclocker Cards - Number of Overclockers * multiplier_milli_buckets_fluid = max millibuckets")
                .defineInRange("multiplier_milli_buckets_fluid", 10000, 0, Integer.MAX_VALUE);
        MIN_TICKS_FLUID = COMMON_BUILDER.comment("Minimum ticks/operation, the first element is with no Logistic Overclockers, the last one is with all four")
        .defineList("min_ticks_fluid", List.of(20, 16, 12, 8, 4), Config::positiveIntegerValidator);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Energy Card").push(SUBCATEGORY_ENERGY);
        MAX_FE_NO_TIERS = COMMON_BUILDER.comment("Maximum FE/operation for Energy Cards (without Energy Overclockers, if they are defined)")
                .defineInRange("max_fe_no_tiers", 1000000, 0, Integer.MAX_VALUE);
        MIN_TICKS_ENERGY = COMMON_BUILDER.comment("Minimum ticks/operation")
                .defineInRange("min_ticks_energy", 1, 1, Integer.MAX_VALUE);

        COMMON_BUILDER.comment("Energy Overclocker Card Tiers (for pack developers)").push(SUBCATEGORY_ENERGY_OVERCLOCKERS);
        MAX_FE_TIERS = COMMON_BUILDER.comment("By adding values to this list, Energy Overclocker(s') will be generated (1 tier for each value).")
                .comment("The maximum FE/operation for each tier is specified using this list. Ex: [512, 2000, 51873]")
                .comment("Note: Since this is a feature meant for pack developers, no recipes will be generated")
                .defineListAllowEmpty("max_fe_tiers", List.of(), Config::positiveIntegerValidator);
        NAME_TIERS = COMMON_BUILDER.comment("By adding values to this list, Energy Overclocker(s') name(s) can be chosen (1 value for each tier).")
                .comment("Normal string rules apply. Ex: [\"name\", \"longer name\", \"NaME wiTh CapiTAL lETters\"]")
                .comment("Note: Default names will be generated if this list is empty/doesn't contain enough elements")
                .defineListAllowEmpty("name_tiers", List.of(), Config::nameValidator);
        COLOR_TIERS = COMMON_BUILDER.comment("By adding values to this list, Energy Overclocker(s') color(s) can be chosen (1 value for each tier).")
                .comment("Each color must be provided as a string using its octal, decimal, or hexadecimal representation.")
                .comment("Example with tier 1 as blue, 2 as green, and 3 as red: [\"0377\", \"65280\", \"#ff0000\"]")
                .comment("Note: Default colors will be generated if this list is empty/doesn't contain enough elements")
                .defineListAllowEmpty("color_tiers", List.of(), Config::colorValidator);
        COMMON_BUILDER.pop(2);

        COMMON_BUILDER.comment("Chemical Card (only if Mekanism is installed)").push(SUBCATEGORY_CHEMICAL);
        BASE_MILLI_BUCKETS_CHEMICAL = COMMON_BUILDER.comment("Millibuckets for Chemical Cards without Overclockers installed")
                .defineInRange("base_milli_buckets_chemical", 15000, 0, Integer.MAX_VALUE);
        MULTIPLIER_MILLI_BUCKETS_CHEMICAL = COMMON_BUILDER.comment("Multiplier for Overclocker Cards - Number of Overclockers * multiplier_milli_buckets_chemical = max millibuckets")
                .defineInRange("multiplier_milli_buckets_chemical", 60000, 0, Integer.MAX_VALUE);
        MIN_TICKS_CHEMICAL = COMMON_BUILDER.comment("Minimum ticks/operation, the first element is with no Logistic Overclockers, the last one is with all four")
        .defineList("min_ticks_chemical", List.of(20, 16, 12, 8, 4), Config::positiveIntegerValidator);
        COMMON_BUILDER.pop(2);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    //For dynamic config
    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .preserveInsertionOrder()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }
}
