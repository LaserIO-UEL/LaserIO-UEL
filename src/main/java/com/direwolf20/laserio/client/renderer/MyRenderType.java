package com.direwolf20.laserio.client.renderer;

import com.direwolf20.laserio.common.LaserIO;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class MyRenderType extends RenderType {
    private static final ResourceLocation SMALL_LASER_BEAM_TEXTURE = new ResourceLocation(LaserIO.MODID, "textures/misc/laser.png");
    private static final ResourceLocation BIG_LASER_BEAM_TEXTURE = new ResourceLocation(LaserIO.MODID, "textures/misc/laser2.png");

    //Dummy
    public MyRenderType(String name, VertexFormat format, VertexFormat.Mode p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable runnablePre, Runnable runnablePost) {
        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, runnablePre, runnablePost);
    }

    public static final RenderType LASER_MAIN_BEAM = create("MiningLaserMainBeam",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setTextureState(new TextureStateShard(BIG_LASER_BEAM_TEXTURE, false, false))
                    .setShaderState(ShaderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                    .setLayeringState(NO_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setCullState(CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(false)
    );
    public static final RenderType LASER_MAIN_CORE = create("MiningLaserCoreBeam",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setTextureState(new TextureStateShard(SMALL_LASER_BEAM_TEXTURE, false, false))
                    .setShaderState(ShaderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setCullState(CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false)
    );
    public static final RenderType CONNECTING_LASER = create("ConnectingLaser",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setTextureState(new TextureStateShard(SMALL_LASER_BEAM_TEXTURE, false, false))
                    .setShaderState(ShaderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setCullState(CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false)
    );
    public static final RenderType BLOCK_OVERLAY = create("MiningLaserBlockOverlay",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setTextureState(NO_TEXTURE)
                    .setShaderState(POSITION_COLOR_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false)
    );
}