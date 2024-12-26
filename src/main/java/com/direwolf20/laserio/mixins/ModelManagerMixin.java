package com.direwolf20.laserio.mixins;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.Registration;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//Taken with permission from Create-Powerlines
@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @ModifyArg(method = "/lambda\\$loadBlockModels\\$10|m_245318_/", require = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;sequence(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;"))
    private static List<CompletableFuture<Pair<ResourceLocation, BlockModel>>>
    laserio$loadBlockModels(List<CompletableFuture<Pair<ResourceLocation, BlockModel>>> list) {
        for (RegistryObject<Item> energyOverclocker : Registration.Energy_Overclocker_Cards) {
            String id = energyOverclocker.getId().getPath();
            list.add(CompletableFuture.supplyAsync(() -> {
                JsonObject root = new JsonObject();
                root.addProperty("parent", "minecraft:item/generated");
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", LaserIO.MODID + ":item/" + id);
                root.add("textures", textures);
                BlockModel model = BlockModel.fromString(root.toString());
                return Pair.of(new ResourceLocation(LaserIO.MODID, "models/item/" + id + ".json"), model);
            }));
        }
        return list;
    }
}