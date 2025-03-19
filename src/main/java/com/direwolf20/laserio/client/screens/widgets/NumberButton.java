package com.direwolf20.laserio.client.screens.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;

import java.awt.Color;

public class NumberButton extends Button {
    private int value;

    public NumberButton(int x, int y, int width, int height, int value, OnPress onPress) {
        super(x, y, width, height, net.minecraft.network.chat.Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.value = value;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF353535);
        guiGraphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1, this.getY() + this.height - 1, 0xFFD8D8D8);
        Font font = Minecraft.getInstance().font;
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        float scale = 0.75f; //value > 99 || value < -99 ? 0.75f : 0.75f;
        stack.scale(scale, scale, scale);
        String msg = String.format("%,d", value);
        float x = (this.getX() + this.width / 2f) / scale - font.width(msg) / 2f;
        float y = (this.getY() + (this.height - font.lineHeight) / 2f / scale) / scale + 1;
        guiGraphics.drawString(font, msg, x, y, Color.DARK_GRAY.getRGB(), false);
        stack.popPose();
    }

    @Override
    public void onClick(double p_onClick_1_, double p_onClick_3_) {
        super.onClick(p_onClick_1_, p_onClick_3_);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return super.mouseClicked(x, y, button);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}