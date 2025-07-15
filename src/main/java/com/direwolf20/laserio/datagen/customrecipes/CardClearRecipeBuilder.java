package com.direwolf20.laserio.datagen.customrecipes;

import com.direwolf20.laserio.setup.Registration;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CardClearRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    @Nullable
    private String group;

    private CardClearRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
    }

    public static CardClearRecipeBuilder shapeless(RecipeCategory category, ItemLike result, int count) {
        return new CardClearRecipeBuilder(category, result, count);
    }

    public static CardClearRecipeBuilder shapeless(RecipeCategory category, ItemLike result) {
        return new CardClearRecipeBuilder(category, result, 1);
    }

    public CardClearRecipeBuilder requires(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    public CardClearRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public CardClearRecipeBuilder requires(TagKey<Item> tag, int quantity) {
        return this.requires(Ingredient.of(tag), quantity);
    }

    public CardClearRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(tag, 1);
    }

    public CardClearRecipeBuilder requires(ItemLike item, int quantity) {
        return this.requires(Ingredient.of(item), quantity);
    }

    public CardClearRecipeBuilder requires(ItemLike item) {
        return this.requires(item, 1);
    }

    @Override
    public CardClearRecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterionTrigger) {
        this.advancement.addCriterion(name, criterionTrigger);

        return this;
    }

    @Override
    public CardClearRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;

        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    private void ensureValid(ResourceLocation consumer) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + consumer);
        }
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        consumer.accept(new CardClearRecipeBuilder.Result(id, this.result, this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.ingredients, this.advancement, id.withPrefix("recipes/misc/")));
    }

    public static class Result extends ShapelessRecipeBuilder.Result {
        public Result(ResourceLocation resourceLocation, Item result, int count, String group, CraftingBookCategory category, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId) {
            super(resourceLocation, result, count, group, category, ingredients, advancement, advancementId);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return Registration.CARD_CLEAR_RECIPE_SERIALIZER.get();
        }
    }
}