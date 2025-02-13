package com.direwolf20.laserio.datagen.loot;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.Registration;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LaserIOBlockLootTables extends BlockLootSubProvider {
    public LaserIOBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(Registration.LaserNode.get());
        dropSelf(Registration.LaserConnector.get());
        dropSelf(Registration.LaserConnectorAdv.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(LaserIO.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}