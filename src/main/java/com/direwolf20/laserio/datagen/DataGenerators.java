package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.common.LaserIO;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = LaserIO.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        //Server side data generators
        generator.addProvider(event.includeServer(), new LaserIORecipes(packOutput));
        generator.addProvider(event.includeServer(), new LaserIOLootTables(packOutput));
        generator.addProvider(event.includeServer(), new LaserIOAdvancements(packOutput));
        LaserIOBlockTags blockTags = new LaserIOBlockTags(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new LaserIOItemTags(packOutput, lookupProvider, blockTags, existingFileHelper));
        //Curios slot and entity generator
        generator.addProvider(event.includeServer(), new LaserIOCuriosData(packOutput, existingFileHelper, lookupProvider));
        //Client side data generators
        generator.addProvider(event.includeClient(), new LaserIOItemModels(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new LaserIOLanguage(packOutput, "en_us"));
    }
}