package com.direwolf20.laserio.datagen.loot;

import com.direwolf20.laserio.common.LaserIO;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import vazkii.patchouli.api.PatchouliAPI;

public class LaserIOAdvancementLootTables implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceLocation, Builder> consumer) {
        Set<ResourceLocation> registeredIds = new HashSet<>();
        generateAdvancementLootTables(lootTable -> {
            ResourceLocation id = lootTable.getId();
            if (!registeredIds.add(id)) {
                throw new IllegalStateException("Duplicate loot table " + id);
            } else {
                consumer.accept(id, lootTable.getBuilder());
            }
        });
    }

    private void generateAdvancementLootTables(Consumer<LootTableWrapper> consumer) {
        ItemStack patchouliBook = PatchouliAPI.get().getBookStack(new ResourceLocation(LaserIO.MODID, "laseriobook"));
        LootTableWrapper.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(patchouliBook.getItem())
                                .apply(SetNbtFunction.setTag(patchouliBook.getOrCreateTag()))))
                .save(consumer, new ResourceLocation(LaserIO.MODID, "advancement_rewards/grant_book"));
    }

    public static class LootTableWrapper {
        private ResourceLocation id;
        private final LootTable.Builder builder;

        public static LootTableWrapper lootTable() {
            return new LootTableWrapper();
        }

        private LootTableWrapper() {
            builder = LootTable.lootTable();
        }

        public LootTableWrapper withPool(LootPool.Builder lootPool) {
            builder.withPool(lootPool);
            return this;
        }

        public void save(Consumer<LootTableWrapper> consumer, ResourceLocation id) {
            this.id = id;
            consumer.accept(this);
        }

        public ResourceLocation getId() {
            return id;
        }

        public LootTable.Builder getBuilder() {
            return builder;
        }
    }
}