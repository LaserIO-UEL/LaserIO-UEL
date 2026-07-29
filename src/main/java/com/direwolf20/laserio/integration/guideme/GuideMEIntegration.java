package com.direwolf20.laserio.integration.guideme;

import com.direwolf20.laserio.common.LaserIO;
import guideme.Guide;
import net.minecraft.resources.ResourceLocation;

public class GuideMEIntegration {
    public static final ResourceLocation GUIDE_ID = new ResourceLocation(LaserIO.MODID, "guide");
    private static Guide guide;

    private GuideMEIntegration() {}

    public static void createGuide() {
        if (guide != null) {
            return;
        }

        guide = Guide.builder(GUIDE_ID)
                .folder("laserio_guidebook")
                .build();
    }
}