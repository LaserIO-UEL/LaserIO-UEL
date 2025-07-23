package com.direwolf20.laserio.util;

import com.direwolf20.laserio.common.LaserIO;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagUtil {
    public static TagKey<Item> createTag(String modId, String tagName) {
        return ItemTags.create(new ResourceLocation(modId, tagName));
    }

    public static TagKey<Item> createLaserIOTag(String tagName) {
        return createTag(LaserIO.MODID, tagName);
    }

    public static TagKey<Item> createForgeTag(String tagName) {
        return createTag("forge", tagName);
    }
}