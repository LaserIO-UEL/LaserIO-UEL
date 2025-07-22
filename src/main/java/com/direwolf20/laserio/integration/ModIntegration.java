package com.direwolf20.laserio.integration;

import net.minecraftforge.fml.ModList;

public enum ModIntegration {
    MEKANISM("mekanism");

    private final String modId;

    private ModIntegration(String modId) {
        this.modId = modId;
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(modId);
    }
}