package com.direwolf20.laserio.common.items.upgrades;

import net.minecraft.world.item.Item;

public class OverclockerCard extends Item {
    private int tier;

    public OverclockerCard(int tier) {
        super(new Item.Properties());

        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }
}