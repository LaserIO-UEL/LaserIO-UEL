package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class LaserIOItemTags extends ItemTagsProvider {
    public static final TagKey<Item> WRENCHES = createTag("forge", "wrenches");
    public static final TagKey<Item> TOOLS_WRENCH = createTag("forge", "tools/wrench");
    public static final TagKey<Item> CIRCUITS_BASIC = createTag("forge", "circuits/basic");
    public static final TagKey<Item> FILTERS = createTag(LaserIO.MODID, "filters");
    public static final TagKey<Item> CURIOS_CARD_HOLDER_SLOT = createTag("curios", "card_holder");

    private static TagKey<Item> createTag(String modid, String tagName) {
        return ItemTags.create(new ResourceLocation(modid, tagName));
    }

    public LaserIOItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), LaserIO.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(WRENCHES)
                .add(Registration.LASER_WRENCH.get());
        tag(TOOLS_WRENCH)
                .add(Registration.LASER_WRENCH.get());
        tag(FILTERS)
                .add(Registration.FILTER_BASIC.get())
                .add(Registration.FILTER_COUNT.get())
                .add(Registration.FILTER_TAG.get())
                .add(Registration.FILTER_MOD.get())
                .add(Registration.FILTER_NBT.get());
        //Add Card Holder to its Curios slot
        tag(CURIOS_CARD_HOLDER_SLOT)
                .add(Registration.CARD_HOLDER.get());
    }
}