package com.direwolf20.laserio.client.screens;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.containers.LaserNodeContainer;
import com.direwolf20.laserio.common.network.PacketHandler;
import com.direwolf20.laserio.common.network.packets.PacketChangeColor;
import com.direwolf20.laserio.common.network.packets.PacketOpenNode;
import com.direwolf20.laserio.util.MiscTools;
import com.direwolf20.laserio.util.Vec2i;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaserNodeSettingsScreen extends Screen {
    private static final ResourceLocation GUI = new ResourceLocation(LaserIO.MODID, "textures/gui/laser_node_settings.png");
    private final LaserNodeContainer container;
    private int imageWidth = 176;
    private int imageHeight = 166;
    private int leftPos;
    private int topPos;
    private int laserRed;
    private int laserGreen;
    private int laserBlue;
    private int laserAlpha;
    private int wrenchAlpha;
    private ForgeSlider sliderRed;
    private ForgeSlider sliderGreen;
    private ForgeSlider sliderBlue;
    private ForgeSlider sliderAlpha;
    private ForgeSlider sliderWrenchAlpha;
    private Map<ForgeSlider, IntConsumer> sliderMap = new HashMap<>();

    public LaserNodeSettingsScreen(LaserNodeContainer container, Component name) {
        super(name);
        this.container = container;
        this.imageHeight = 181;
        Color color = container.tile.getColor();
        laserRed = color.getRed();
        laserGreen = color.getGreen();
        laserBlue = color.getBlue();
        laserAlpha = color.getAlpha();
        wrenchAlpha = container.tile.getWrenchAlpha();
    }

    @Override
    public void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        List<AbstractWidget> leftWidgets = new ArrayList<>();

        if (container.side != -1) {
            Button returnButton = new ExtendedButton(getGuiLeft() - 25, getGuiTop() + 1, 25, 20, Component.literal("<--"), (button) -> {
                openTab(container.side);
            });
            leftWidgets.add(returnButton);
        }

        Button applyButton = new ExtendedButton(getGuiLeft() + 25, getGuiTop() + 150, 50, 20, Component.translatable("screen.laserio.apply"), (button) -> {
            syncColors();
        });
        leftWidgets.add(applyButton);

        Button defaultButton = new ExtendedButton(getGuiLeft() + 100, getGuiTop() + 150, 50, 20, Component.translatable("screen.laserio.default"), (button) -> {
            Color defaultColor = container.tile.getDefaultColor();
            laserRed = defaultColor.getRed();
            sliderRed.setValue(laserRed);
            laserGreen = defaultColor.getGreen();
            sliderGreen.setValue(laserGreen);
            laserBlue = defaultColor.getBlue();
            sliderBlue.setValue(laserBlue);
            laserAlpha = defaultColor.getAlpha();
            sliderAlpha.setValue(laserAlpha);
            wrenchAlpha = 0;
            sliderWrenchAlpha.setValue(0);
            syncColors();
        });
        leftWidgets.add(defaultButton);

        sliderRed = new ForgeSlider(getGuiLeft() + 15, getGuiTop() + 45, 150, 15, Component.translatable("screen.laserio.red").append(": "), Component.empty(), 0, 255, this.laserRed, true) {
            @Override
            protected void applyValue() {
                laserRed = this.getValueInt();
            }
        };
        leftWidgets.add(sliderRed);
        sliderGreen = new ForgeSlider(getGuiLeft() + 15, getGuiTop() + 65, 150, 15, Component.translatable("screen.laserio.green").append(": "), Component.empty(), 0, 255, this.laserGreen, true) {
            @Override
            protected void applyValue() {
                laserGreen = this.getValueInt();
            }
        };
        leftWidgets.add(sliderGreen);
        sliderBlue = new ForgeSlider(getGuiLeft() + 15, getGuiTop() + 85, 150, 15, Component.translatable("screen.laserio.blue").append(": "), Component.empty(), 0, 255, this.laserBlue, true) {
            @Override
            protected void applyValue() {
                laserBlue = this.getValueInt();
            }
        };
        leftWidgets.add(sliderBlue);
        sliderAlpha = new ForgeSlider(getGuiLeft() + 15, getGuiTop() + 105, 150, 15, Component.translatable("screen.laserio.alpha").append(": "), Component.empty(), 0, 255, this.laserAlpha, true) {
            @Override
            protected void applyValue() {
                laserAlpha = this.getValueInt();
            }
        };
        leftWidgets.add(sliderAlpha);
        sliderWrenchAlpha = new ForgeSlider(getGuiLeft() + 15, getGuiTop() + 125, 150, 15, Component.translatable("screen.laserio.wrench").append(": "), Component.empty(), 0, 255, this.wrenchAlpha, true) {
            @Override
            protected void applyValue() {
                wrenchAlpha = this.getValueInt();
            }
        };
        leftWidgets.add(sliderWrenchAlpha);

        for (int i = 0; i < leftWidgets.size(); i++) {
            addRenderableWidget(leftWidgets.get(i));
        }

        // Used for scroll action
        this.sliderMap = Map.of(
                sliderRed, (a) -> laserRed = a,
                sliderGreen, (a) -> laserGreen = a,
                sliderBlue, (a) -> laserBlue = a,
                sliderAlpha, (a) -> laserAlpha = a,
                sliderWrenchAlpha, (a) -> wrenchAlpha = a
        );
    }

    private void syncColors() {
        PacketHandler.sendToServer(new PacketChangeColor(container.tile.getBlockPos(), new Color(laserRed, laserGreen, laserBlue, laserAlpha).getRGB(), wrenchAlpha));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        this.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderLabels(guiGraphics, mouseX, mouseY);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(getGuiLeft(), getGuiTop(), 0);
        String settings = Component.translatable("screen.laserio.network_settings").getString();
        int color = Color.DARK_GRAY.getRGB();
        guiGraphics.drawString(font, settings, imageWidth / 2 - font.width(settings) / 2, 20, color, false);
        guiGraphics.drawString(font, "U", 15, 7, color, false);
        guiGraphics.drawString(font, "D", 43, 7, color, false);
        guiGraphics.drawString(font, "N", 71, 7, color, false);
        guiGraphics.drawString(font, "S", 99, 7, color, false);
        guiGraphics.drawString(font, "W", 127, 7, color, false);
        guiGraphics.drawString(font, "E", 155, 7, color, false);
        for (Direction direction : Direction.values()) {
            ItemStack itemStack = getAdjacentBlock(direction);
            if (!itemStack.isEmpty()) {
                Vec2i tab = LaserNodeScreen.TABS[direction.ordinal()];
                guiGraphics.renderItem(itemStack, tab.x + 4, tab.y - 14, 0);
                if (MiscTools.inBounds(getGuiLeft() + tab.x + 4, getGuiTop() + tab.y - 14, 16, 16, mouseX, mouseY)) {
                    guiGraphics.renderTooltip(font, itemStack, mouseX - getGuiLeft(), mouseY - getGuiTop());
                }
            }
        }
        guiGraphics.pose().translate(0, 0, 100);
        int startX = 15;
        int startY = 30;
        guiGraphics.fill(startX, startY, startX + 150, startY + 10, new Color(laserRed, laserGreen, laserBlue, laserAlpha).getRGB());
        guiGraphics.pose().popPose();
    }

    protected ItemStack getAdjacentBlock(Direction direction) {
        BlockState blockState = container.playerEntity.level().getBlockState(this.container.tile.getBlockPos().relative(direction));
        ItemStack itemStack = blockState.getBlock().asItem().getDefaultInstance();
        return itemStack;
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
        int tabOffset = LaserNodeScreen.TABS[container.side].x - 2;
        guiGraphics.blit(LaserNodeScreen.SELECTED_TABS_OVERLAY, relX + tabOffset, relY, tabOffset, 0, 28, 24);
    }

    private void openTab(byte tabIndex) {
        PacketHandler.sendToServer(new PacketOpenNode(container.tile.getBlockPos(), tabIndex));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.sliderMap.forEach((slider, consumer) -> {
            if (slider.isMouseOver(mouseX, mouseY)) {
                slider.setValue(slider.getValueInt() + (delta > 0 ? 1 : -1));
                consumer.accept(slider.getValueInt());
            }
        });
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        for (byte i = 0; i < LaserNodeScreen.TABS.length; i++) {
            Vec2i tab = LaserNodeScreen.TABS[i];
            if (MiscTools.inBounds(getGuiLeft() + tab.x, getGuiTop() + tab.y, 24, 12, x, y)) {
                openTab(i);
                return true;
            }
        }
        return super.mouseClicked(x, y, btn);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public int getGuiLeft() {
        return leftPos;
    }

    public int getGuiTop() {
        return topPos;
    }
}