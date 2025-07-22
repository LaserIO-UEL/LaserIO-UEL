package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.datagen.customrecipes.CardClearRecipeBuilder;
import com.direwolf20.laserio.setup.Registration;
import com.direwolf20.laserio.util.TagUtil;
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class LaserIORecipes extends RecipeProvider implements IConditionBuilder {
    private static final TagKey<Item> CIRCUITS_BASIC = TagUtil.createForgeTag("circuits/basic");

    public LaserIORecipes(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        //Crafting components
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.LOGIC_CHIP_RAW.get(), 4)
                .pattern("rgr")
                .pattern("cqc")
                .pattern("rgr")
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('q', Tags.Items.STORAGE_BLOCKS_QUARTZ)
                .define('g', Tags.Items.NUGGETS_GOLD)
                .define('c', Items.CLAY_BALL)
                .unlockedBy("has_quartz", TriggerInstance.hasItems(Items.QUARTZ_BLOCK))
                .save(consumer);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.LOGIC_CHIP_RAW.get()), RecipeCategory.MISC, Registration.LOGIC_CHIP.get(), 1.0f, 100)
                .unlockedBy("has_raw_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP_RAW.get()))
                .save(consumer);

        //Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.LASER_CONNECTOR_BLOCK.get(), 1)
                .pattern(" g ")
                .pattern("rbr")
                .pattern("iii")
                .define('g', Tags.Items.GLASS)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('b', Registration.LOGIC_CHIP.get())
                .group("laserio_network_blocks")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.LASER_CONNECTOR_ADV_BLOCK.get(), 1)
                .pattern("ede")
                .pattern("rbr")
                .pattern("iii")
                .define('d', Tags.Items.GEMS_DIAMOND)
                .define('e', Tags.Items.ENDER_PEARLS)
                .define('i', Tags.Items.INGOTS_GOLD)
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('b', Registration.LASER_CONNECTOR_ITEM.get())
                .group("laserio_network_blocks")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.LASER_NODE_BLOCK.get(), 1)
                .pattern("igi")
                .pattern("gbg")
                .pattern("igi")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('g', Tags.Items.GLASS_PANES)
                .define('b', Registration.LASER_CONNECTOR_BLOCK.get())
                .group("laserio_network_blocks")
                .unlockedBy("has_laser_connector", TriggerInstance.hasItems(Registration.LASER_CONNECTOR_BLOCK.get()))
                .save(consumer);

        //Tools
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.LASER_WRENCH.get(), 1)
                .pattern("i i")
                .pattern(" b ")
                .pattern(" i ")
                .define('b', Registration.LOGIC_CHIP.get())
                .define('i', Tags.Items.INGOTS_IRON)
                .group("laserio_tools")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CARD_HOLDER.get(), 1)
                .pattern("i i")
                .pattern("cbc")
                .pattern("i i")
                .define('b', Registration.LOGIC_CHIP.get())
                .define('i', Tags.Items.INGOTS_IRON)
                .define('c', Tags.Items.CHESTS)
                .group("laserio_tools")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CARD_CLONER.get(), 1)
                .pattern("i i")
                .pattern("cbc")
                .pattern("i i")
                .define('b', Registration.LOGIC_CHIP.get())
                .define('i', Tags.Items.INGOTS_IRON)
                .define('c', Items.PAPER)
                .group("laserio_tools")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);

        //Cards
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CARD_ITEM.get(), 1)
                .pattern("rlr")
                .pattern("qpq")
                .pattern("ggg")
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('p', Registration.LOGIC_CHIP.get())
                .define('g', Tags.Items.NUGGETS_GOLD)
                .define('l', Tags.Items.GEMS_LAPIS)
                .define('q', Tags.Items.GEMS_QUARTZ)
                .group("laserio_cards")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CARD_FLUID.get(), 1)
                .pattern("rlr")
                .pattern("qpq")
                .pattern("ggg")
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('p', Registration.LOGIC_CHIP.get())
                .define('g', Tags.Items.NUGGETS_GOLD)
                .define('l', Items.BUCKET)
                .define('q', Tags.Items.GEMS_QUARTZ)
                .group("laserio_cards")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CARD_ENERGY.get(), 1)
                .pattern("rlr")
                .pattern("qpq")
                .pattern("ggg")
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('p', Registration.LOGIC_CHIP.get())
                .define('g', Tags.Items.NUGGETS_GOLD)
                .define('l', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('q', Tags.Items.GEMS_QUARTZ)
                .group("laserio_cards")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CARD_REDSTONE.get(), 1)
                .pattern("rrr")
                .pattern("qpq")
                .pattern("ggg")
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('p', Registration.LOGIC_CHIP.get())
                .define('g', Tags.Items.NUGGETS_GOLD)
                .define('q', Tags.Items.GEMS_QUARTZ)
                .group("laserio_cards")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);

        //Mekanism Card
        ConditionalRecipe.builder()
                .addCondition(modLoaded("mekanism"))
                .addRecipe(t ->
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CARD_CHEMICAL.get(), 1)
                            .pattern("rlr")
                            .pattern("qpq")
                            .pattern("ggg")
                            .define('r', Tags.Items.DUSTS_REDSTONE)
                            .define('p', Registration.LOGIC_CHIP.get())
                            .define('g', Tags.Items.NUGGETS_GOLD)
                            .define('l', CIRCUITS_BASIC)
                            .define('q', Tags.Items.GEMS_QUARTZ)
                            .group("laserio_cards")
                            .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                            .save(t))
                .generateAdvancement(Registration.CARD_CHEMICAL.getId().withPrefix("recipes/misc/"))
                .build(consumer, Registration.CARD_CHEMICAL.getId());

        //Filters
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.FILTER_BASIC.get(), 4)
                .pattern("igi")
                .pattern("gqg")
                .pattern("igi")
                .define('i', Items.IRON_BARS)
                .define('q', Registration.LOGIC_CHIP.get())
                .define('g', Tags.Items.GLASS_PANES)
                .group("laserio_filters")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.FILTER_COUNT.get(), 1)
                .requires(Registration.FILTER_BASIC.get(), 1)
                .requires(Items.OBSERVER, 1)
                .group("laserio_filters")
                .unlockedBy("has_filter_basic", TriggerInstance.hasItems(Registration.FILTER_BASIC.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.FILTER_TAG.get(), 1)
                .requires(Registration.FILTER_BASIC.get(), 1)
                .requires(Items.PAPER, 1)
                .group("laserio_filters")
                .unlockedBy("has_filter_basic", TriggerInstance.hasItems(Registration.FILTER_BASIC.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.FILTER_NBT.get(), 1)
                .requires(Registration.FILTER_BASIC.get(), 1)
                .requires(Items.WHITE_WOOL, 1)
                .group("laserio_filters")
                .unlockedBy("has_filter_basic", TriggerInstance.hasItems(Registration.FILTER_BASIC.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.FILTER_MOD.get(), 1)
                .requires(Registration.FILTER_BASIC.get(), 1)
                .requires(Items.BOOK, 1)
                .group("laserio_filters")
                .unlockedBy("has_filter_basic", TriggerInstance.hasItems(Registration.FILTER_BASIC.get()))
                .save(consumer);

        //Upgrades
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.OVERCLOCKER_NODE.get(), 1)
                .pattern(" g ")
                .pattern("rpr")
                .pattern("ggg")
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('p', Registration.LOGIC_CHIP.get())
                .define('g', Tags.Items.GEMS_DIAMOND)
                .group("laserio_overclockers")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.LOGISTIC_OVERCLOCKER_CARD.get(), 1)
                .pattern(" g ")
                .pattern("rpr")
                .pattern("ggg")
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('p', Registration.LOGIC_CHIP.get())
                .define('g', Tags.Items.INGOTS_GOLD)
                .group("laserio_overclockers")
                .unlockedBy("has_logic_chip", TriggerInstance.hasItems(Registration.LOGIC_CHIP.get()))
                .save(consumer);

        //Card NBT clearing recipes
        CardClearRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.CARD_ITEM.get())
                .requires(Registration.CARD_ITEM.get())
                .group("laserio_cards")
                .unlockedBy("has_card_item", TriggerInstance.hasItems(Registration.CARD_ITEM.get()))
                .save(consumer, Registration.CARD_ITEM.getId().withSuffix("_nbtclear"));
        CardClearRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.CARD_FLUID.get())
                .requires(Registration.CARD_FLUID.get())
                .group("laserio_cards")
                .unlockedBy("has_card_fluid", TriggerInstance.hasItems(Registration.CARD_FLUID.get()))
                .save(consumer, Registration.CARD_FLUID.getId().withSuffix("_nbtclear"));
        CardClearRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.CARD_ENERGY.get())
                .requires(Registration.CARD_ENERGY.get())
                .group("laserio_cards")
                .unlockedBy("has_card_energy", TriggerInstance.hasItems(Registration.CARD_ENERGY.get()))
                .save(consumer, Registration.CARD_ENERGY.getId().withSuffix("_nbtclear"));
        CardClearRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.CARD_REDSTONE.get())
                .requires(Registration.CARD_REDSTONE.get())
                .group("laserio_cards")
                .unlockedBy("has_card_redstone", TriggerInstance.hasItems(Registration.CARD_REDSTONE.get()))
                .save(consumer, Registration.CARD_REDSTONE.getId().withSuffix("_nbtclear"));

        //Mekanism Card NBT clearing recipe
        ConditionalRecipe.builder()
                .addCondition(modLoaded("mekanism"))
                .addRecipe(t ->
                    CardClearRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.CARD_CHEMICAL.get())
                            .requires(Registration.CARD_CHEMICAL.get())
                            .group("laserio_cards")
                            .unlockedBy("has_card_chemical", TriggerInstance.hasItems(Registration.CARD_CHEMICAL.get()))
                            .save(t))
                .generateAdvancement(Registration.CARD_CHEMICAL.getId().withPrefix("recipes/misc/").withSuffix("_nbtclear"))
                .build(consumer, Registration.CARD_CHEMICAL.getId().withSuffix("_nbtclear"));

        //Filter NBT clearing recipes
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.FILTER_BASIC.get())
                .requires(Registration.FILTER_BASIC.get())
                .group("laserio_filters")
                .unlockedBy("has_filter_basic", TriggerInstance.hasItems(Registration.FILTER_BASIC.get()))
                .save(consumer, Registration.FILTER_BASIC.getId().withSuffix("_nbtclear"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.FILTER_COUNT.get())
                .requires(Registration.FILTER_COUNT.get())
                .group("laserio_filters")
                .unlockedBy("has_filter_count", TriggerInstance.hasItems(Registration.FILTER_COUNT.get()))
                .save(consumer, Registration.FILTER_COUNT.getId().withSuffix("_nbtclear"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.FILTER_TAG.get())
                .requires(Registration.FILTER_TAG.get())
                .group("laserio_filters")
                .unlockedBy("has_filter_tag", TriggerInstance.hasItems(Registration.FILTER_TAG.get()))
                .save(consumer, Registration.FILTER_TAG.getId().withSuffix("_nbtclear"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.FILTER_NBT.get())
                .requires(Registration.FILTER_NBT.get())
                .group("laserio_filters")
                .unlockedBy("has_nbt_tag", TriggerInstance.hasItems(Registration.FILTER_NBT.get()))
                .save(consumer, Registration.FILTER_NBT.getId().withSuffix("_nbtclear"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.FILTER_MOD.get())
                .requires(Registration.FILTER_MOD.get())
                .group("laserio_filters")
                .unlockedBy("has_filter_mod", TriggerInstance.hasItems(Registration.FILTER_MOD.get()))
                .save(consumer, Registration.FILTER_MOD.getId().withSuffix("_nbtclear"));
    }
}