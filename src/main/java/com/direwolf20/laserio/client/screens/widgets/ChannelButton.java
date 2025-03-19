package com.direwolf20.laserio.client.screens.widgets;

import com.direwolf20.laserio.common.LaserIO;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.Color;

public class ChannelButton extends Button {
    private static final ResourceLocation TEXTURE = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/blankbutton.png");
    private static final Color[] COLORS = {
            new Color(0xf9ffff),
            new Color(0xf9801d),
            new Color(0xc64fbd),
            new Color(0x3ab3da),
            new Color(0xffd83d),
            new Color(0x80c71f),
            new Color(0xf38caa),
            new Color(0x474f52),
            new Color(0x9c9d97),
            new Color(0x169c9d),
            new Color(0x8932b7),
            new Color(0x3c44a9),
            new Color(0x825432),
            new Color(0x5d7c15),
            new Color(0xb02e26),
            new Color(0x1d1c21)
    };
    private int channel;

    public ChannelButton(int widthIn, int heightIn, int width, int height, int channel, OnPress onPress) {
        super(widthIn, heightIn, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.channel = channel;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, 0, width, height, width, height);
        guiGraphics.fill(this.getX() + 4, this.getY() + 4, this.getX() + this.width - 4, this.getY() + this.height - 4, COLORS[channel].getRGB());
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}