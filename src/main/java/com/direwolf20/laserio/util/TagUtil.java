package com.direwolf20.laserio.util;

import com.direwolf20.laserio.common.LaserIO;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class TagUtil {
    public static TagKey<Item> createItemTag(String tag) {
        return ItemTags.create(new ResourceLocation(tag));
    }

    public static TagKey<Item> createItemTag(String modId, String tag) {
        return ItemTags.create(new ResourceLocation(modId, tag));
    }

    public static TagKey<Item> createForgeItemTag(String tag) {
        return createItemTag("forge", tag);
    }

    public static TagKey<Item> createLaserIOItemTag(String tag) {
        return createItemTag(LaserIO.MODID, tag);
    }

    public static TagKey<Fluid> createFluidTag(String tag) {
        return FluidTags.create(new ResourceLocation(tag));
    }
}