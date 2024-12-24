package com.direwolf20.laserio.common.items.upgrades;

import net.minecraft.world.item.Item;

public class OverclockerCard extends Item {
    private int energyTier;

    public OverclockerCard(int tier) {
        super(new Item.Properties());

        this.energyTier = tier;
    }

    public int getEnergyTier() {
        return energyTier;
    }
}