package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.common.LaserIO;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import top.theillusivec4.curios.api.CuriosDataProvider;

public class LaserIOCuriosData extends CuriosDataProvider {
    public LaserIOCuriosData(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<Provider> registries) {
        super(LaserIO.MODID, output, fileHelper, registries);
    }

    @Override
    public void generate(Provider registries, ExistingFileHelper fileHelper) {
        createSlot("card_holder")
                .size(1)
                .icon(new ResourceLocation(LaserIO.MODID, "item/empty_card_holder_slot"))
                .renderToggle(false);
        createEntities("card_holder")
                .addPlayer()
                .addSlots("card_holder");
    }
}