package com.direwolf20.laserio.setup;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.List;

@EventBusSubscriber
public class Config {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static final String CATEGORY_CARD = "card";
    public static final String SUBCATEGORY_FLUID = "fluid_card";
    public static final String SUBCATEGORY_ENERGY = "energy_card";
    public static final String SUBCATEGORY_CHEMICAL = "chemical_card";

    public static ForgeConfigSpec.IntValue BASE_MILLI_BUCKETS_FLUID;
    public static ForgeConfigSpec.IntValue MULTIPLIER_MILLI_BUCKETS_FLUID;
    public static ForgeConfigSpec.IntValue MAX_FE_TICK_NO_TIERS;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> MAX_FE_TICK_TIERS;
    public static ForgeConfigSpec.IntValue BASE_MILLI_BUCKETS_CHEMICAL;
    public static ForgeConfigSpec.IntValue MULTIPLIER_MILLI_BUCKETS_CHEMICAL;

    private static boolean maxFeTickValidator(Object obj) {
        if (obj instanceof Integer maxFeTick)
            return (maxFeTick > 0);

        return false;
    }

    static {
        COMMON_BUILDER.comment("Card settings").push(CATEGORY_CARD);

        COMMON_BUILDER.comment("Fluid Card").push(SUBCATEGORY_FLUID);
        BASE_MILLI_BUCKETS_FLUID = COMMON_BUILDER.comment("Millibuckets for Fluid Cards without Overclockers installed")
                .defineInRange("base_milli_buckets_fluid", 5000, 0, Integer.MAX_VALUE);
        MULTIPLIER_MILLI_BUCKETS_FLUID = COMMON_BUILDER.comment("Multiplier for Overclocker Cards - Number of Overclockers * this value = max millibuckets")
                .defineInRange("multiplier_milli_buckets_fluid", 10000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Energy Card").push(SUBCATEGORY_ENERGY);
        MAX_FE_TICK_NO_TIERS = COMMON_BUILDER.comment("Maximum FE/t for Energy Cards (this value is used only if the Energy Overclocker Card Tiers' list is empty)")
                .defineInRange("max_fe_tick_no_tiers", 1000000, 0, Integer.MAX_VALUE);
        MAX_FE_TICK_TIERS = COMMON_BUILDER.comment("By adding values to this list, Energy Overclockers will be generated (1 tier for each value).")
                .comment("The maximum FE/t for each tier is specified using this list.")
                .comment("Note: this is a feature meant for pack developers, so default recipes won't be generated")
                .defineListAllowEmpty("max_fe_tick_tiers", List.of(), Config::maxFeTickValidator);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Chemical Card").push(SUBCATEGORY_CHEMICAL);
        BASE_MILLI_BUCKETS_CHEMICAL = COMMON_BUILDER.comment("Millibuckets for Chemical Cards without Overclockers installed (only if Mekanism is installed)")
                .defineInRange("base_milli_buckets_chemical", 15000, 0, Integer.MAX_VALUE);
        MULTIPLIER_MILLI_BUCKETS_CHEMICAL = COMMON_BUILDER.comment("Multiplier for Overclocker Cards - Number of Overclockers * this value = max millibuckets  (only if Mekanism is installed)")
                .defineInRange("multiplier_milli_buckets_chemical", 60000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();
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
}