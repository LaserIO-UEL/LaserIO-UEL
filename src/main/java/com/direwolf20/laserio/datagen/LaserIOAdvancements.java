package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.Registration;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.ConditionalAdvancement;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class LaserIOAdvancements implements DataProvider, IConditionBuilder {
    private final PathProvider pathProvider;

    public LaserIOAdvancements(PackOutput output) {
        pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Set<ResourceLocation> registeredIds = new HashSet<>();
        List<CompletableFuture<?>> futures = new ArrayList<>();
        generateConditionalAdvancements(advancement -> {
            ResourceLocation id = advancement.getId();
            if (!registeredIds.add(id)) {
                throw new IllegalStateException("Duplicate advancement " + id);
            } else {
                futures.add(DataProvider.saveStable(cache, advancement.serializeToJson(), pathProvider.json(id)));
            }
        });
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private void generateConditionalAdvancements(Consumer<ConditionalAdvancementWrapper> consumer) {
        ConditionalAdvancementWrapper.builder()
                .addCondition(modLoaded("patchouli"))
                .addAdvancement(Advancement.Builder.advancement()
                        .addCriterion("obtain_laser_node", InventoryChangeTrigger.TriggerInstance.hasItems(Registration.LaserNode_ITEM.get()))
                        .rewards(AdvancementRewards.Builder.loot(new ResourceLocation(LaserIO.MODID, "advancement_rewards/grant_book"))))
                .save(consumer, new ResourceLocation(LaserIO.MODID, "grant_book"));
    }

    @Override
    public String getName() {
        return "LaserIO Advancements";
    }

    public static class ConditionalAdvancementWrapper {
        private ResourceLocation id;
        private final ConditionalAdvancement.Builder builder;

        public static ConditionalAdvancementWrapper builder() {
            return new ConditionalAdvancementWrapper();
        }

        private ConditionalAdvancementWrapper() {
            builder = ConditionalAdvancement.builder();
        }

        public ConditionalAdvancementWrapper addCondition(ICondition condition) {
            builder.addCondition(condition);
            return this;
        }

        public ConditionalAdvancementWrapper addAdvancement(Advancement.Builder advancement) {
            builder.addAdvancement(advancement);
            return this;
        }

        public void save(Consumer<ConditionalAdvancementWrapper> consumer, ResourceLocation id) {
            this.id = id;
            consumer.accept(this);
        }

        public ResourceLocation getId() {
            return id;
        }

        public JsonObject serializeToJson() {
            return builder.write();
        }
    }
}