package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.datagen.loot.LaserIOAdvancementLootTables;
import com.direwolf20.laserio.datagen.loot.LaserIOBlockLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class LaserIOLootTables extends LootTableProvider {
    public LaserIOLootTables(PackOutput packOutput) {
        super(packOutput, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(LaserIOAdvancementLootTables::new, LootContextParamSets.ADVANCEMENT_REWARD),
                new LootTableProvider.SubProviderEntry(LaserIOBlockLootTables::new, LootContextParamSets.BLOCK)));
    }
}