package com.direwolf20.laserio.common.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class LIOUregistration {
    public static final LIOURegistrate REGISTRATE = LIOURegistrate.create("LIOUnoffical");
    static {

        LIOUregistration.REGISTRATE.defaultCreativeTab((ResourceKey< CreativeModeTab>);

    }

    private LIOUregistration() {}
}
