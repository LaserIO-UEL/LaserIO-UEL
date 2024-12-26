package com.direwolf20.laserio.mixins;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.items.upgrades.OverclockerCard;
import com.direwolf20.laserio.setup.Registration;
import com.direwolf20.laserio.util.MixinUtil;
import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import org.jline.utils.Colors;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;

//Taken with permission from Create-Powerlines
@Mixin(DirectoryLister.class)
public class DirectorySpriteSourceMixin {
    @Final
    @Shadow
    private String sourcePath;

    @Inject(method = "run", require = 1, at = @At("HEAD"))
    private void laserio$load(ResourceManager resMgr, SpriteSource.Output out, CallbackInfo ci) {
        if (sourcePath.equals("block")) {
            for (RegistryObject<Item> energyOverclocker : Registration.Energy_Overclocker_Cards) {
                String id = energyOverclocker.getId().getPath();
                int energyTier = ((OverclockerCard) energyOverclocker.get()).getEnergyTier();
                ResourceLocation loc = new ResourceLocation(LaserIO.MODID, "item/" + id);
                out.add(loc, new SpriteSource.SpriteSupplier() {
                    public SpriteContents get() {
                        NativeImage base = MixinUtil.loadNativeImage("item/energy_overclocker_card");
                        NativeImage overlay = MixinUtil.loadNativeImage("template/energy_overclocker_card_overlay");
                        long basePixels = MixinUtil.getPixels(base);
                        long overlayPixels = MixinUtil.getPixels(overlay);
                        IntBuffer baseBuffer = MemoryUtil.memIntBuffer(basePixels, 256);
                        IntBuffer overlayBuffer = MemoryUtil.memIntBuffer(overlayPixels, 256);
                        int color = Colors.DEFAULT_COLORS_256[energyTier % 256];
                        for (int i = 0; i < 256; i++) {
                            baseBuffer.put(i, MixinUtil.blendColor(baseBuffer.get(i), MixinUtil.tintColor(overlayBuffer.get(i), color)));
                        }
                        overlay.close();
                        return new SpriteContents(loc, new FrameSize(16, 16), base, AnimationMetadataSection.EMPTY, null);
                    }
                });
            }
        }
    }
}