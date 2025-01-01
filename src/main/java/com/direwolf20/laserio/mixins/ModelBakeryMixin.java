package com.direwolf20.laserio.mixins;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.Registration;
import com.google.gson.JsonObject;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//Partially based on code taken with permission from Create-Powerlines
@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
    /**
     * @author embeddedt
     * @reason hijack normal loading from files if the model is an energy overclocker
     */
    @Inject(method = "loadBlockModel", at = @At("HEAD"), cancellable = true)
    private void laserio$loadOverclockerModel(ResourceLocation location, CallbackInfoReturnable<BlockModel> cir) {
        if (!Registration.Energy_Overclocker_Cards.isEmpty() && location.getNamespace().equals(LaserIO.MODID) && location.getPath().startsWith("item/energy_overclocker_card_tier_")) {
            String id = location.getPath().replaceFirst("^item/", "");
            JsonObject root = new JsonObject();
            root.addProperty("parent", "minecraft:item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", LaserIO.MODID + ":item/" + id);
            root.add("textures", textures);
            BlockModel model = BlockModel.fromString(root.toString());
            model.name = location.toString();
            cir.setReturnValue(model);
        }
    }
}