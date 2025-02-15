package com.direwolf20.laserio.mixins;

import com.direwolf20.laserio.setup.Config;
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
import org.jline.utils.Colors;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;
import java.util.stream.IntStream;

//Taken with permission from Create-Powerlines
@Mixin(DirectoryLister.class)
public class DirectorySpriteSourceMixin {
    @Final
    @Shadow
    private String sourcePath;

    @Inject(method = "run", require = 1, at = @At("HEAD"))
    private void laserio$loadEnergyOverclockersTextures(ResourceManager resMgr, SpriteSource.Output out, CallbackInfo ci) {
        if (sourcePath.equals("block")) {
            IntStream.range(0, Registration.Energy_Overclocker_Cards.size())
                    .forEach(i -> {
                        ResourceLocation id = Registration.Energy_Overclocker_Cards.get(i).getId().withPrefix("item/");
                        out.add(id, new SpriteSource.SpriteSupplier() {
                            public SpriteContents get() {
                                NativeImage base = MixinUtil.loadNativeImage("item/energy_overclocker_card");
                                NativeImage overlay = MixinUtil.loadNativeImage("template/energy_overclocker_card_overlay");
                                long basePixels = MixinUtil.getPixels(base);
                                long overlayPixels = MixinUtil.getPixels(overlay);
                                IntBuffer baseBuffer = MemoryUtil.memIntBuffer(basePixels, 256);
                                IntBuffer overlayBuffer = MemoryUtil.memIntBuffer(overlayPixels, 256);
                                int color = (i < Config.COLOR_TIERS.get().size()) ? Integer.decode(Config.COLOR_TIERS.get().get(i)) : Colors.DEFAULT_COLORS_256[i % 256];
                                for (int j = 0; j < 256; j++) {
                                    baseBuffer.put(j, MixinUtil.blendColor(baseBuffer.get(j), MixinUtil.tintColor(overlayBuffer.get(j), color)));
                                }
                                overlay.close();
                                return new SpriteContents(id, new FrameSize(16, 16), base, AnimationMetadataSection.EMPTY, null);
                            }
                        });
                    });
        }
    }
}