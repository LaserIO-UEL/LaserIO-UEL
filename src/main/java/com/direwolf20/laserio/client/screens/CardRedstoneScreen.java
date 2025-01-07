package com.direwolf20.laserio.client.screens;

import com.direwolf20.laserio.client.screens.widgets.ChannelButton;
import com.direwolf20.laserio.client.screens.widgets.NumberButton;
import com.direwolf20.laserio.client.screens.widgets.ToggleButton;
import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.containers.CardRedstoneContainer;
import com.direwolf20.laserio.common.items.cards.CardRedstone;
import com.direwolf20.laserio.common.network.PacketHandler;
import com.direwolf20.laserio.common.network.packets.PacketOpenNode;
import com.direwolf20.laserio.common.network.packets.PacketUpdateRedstoneCard;
import com.direwolf20.laserio.util.MiscTools;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.HashMap;
import java.util.Map;

public class CardRedstoneScreen extends AbstractContainerScreen<CardRedstoneContainer> {
    private final ResourceLocation GUI = new ResourceLocation(LaserIO.MODID, "textures/gui/redstonecard.png");

    protected final CardRedstoneContainer container;
    protected byte currentMode;
    protected byte currentRedstoneChannel;
    protected boolean currentStrong;
    protected boolean currentInvert;
    protected boolean currentThreshold;
    protected byte currentThresholdLimit;
    protected byte currentThresholdOutput;
    protected boolean currentBlockRedstone;
    protected final ItemStack card;
    protected Map<String, Button> buttons = new HashMap<>();

    public CardRedstoneScreen(CardRedstoneContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        this.card = container.cardItem;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        Button modeButton = buttons.get("mode");
        if (MiscTools.inBounds(modeButton.getX(), modeButton.getY(), modeButton.getWidth(), modeButton.getHeight(), mouseX, mouseY)) {
            MutableComponent translatableComponents[] = new MutableComponent[3];
            translatableComponents[0] = Component.translatable("screen.laserio.input");
            translatableComponents[1] = Component.translatable("screen.laserio.output");
            guiGraphics.renderTooltip(font, translatableComponents[currentMode], mouseX, mouseY);
        }
        if (currentMode == 0) {
            Button invertButton = buttons.get("invert");
            if (MiscTools.inBounds(invertButton.getX(), invertButton.getY(), invertButton.getWidth(), invertButton.getHeight(), mouseX, mouseY)) {
                MutableComponent translatableComponents[] = new MutableComponent[2];
                translatableComponents[0] = Component.translatable("screen.laserio.notinvert");
                translatableComponents[1] = Component.translatable("screen.laserio.invert");
                guiGraphics.renderTooltip(font, translatableComponents[currentInvert ? 1 : 0], mouseX, mouseY);
            }
            Button thresholdToggleButton = buttons.get("thresholdtoggle");
            if (MiscTools.inBounds(thresholdToggleButton.getX(), thresholdToggleButton.getY(), thresholdToggleButton.getWidth(), thresholdToggleButton.getHeight(), mouseX, mouseY)) {
                MutableComponent translatableComponents[] = new MutableComponent[2];
                translatableComponents[0] = Component.translatable("screen.laserio.notthreshold");
                translatableComponents[1] = Component.translatable("screen.laserio.threshold");
                guiGraphics.renderTooltip(font, translatableComponents[currentThreshold ? 1 : 0], mouseX, mouseY);
            }
            /*
            Button blockRedstoneButton = buttons.get("blockredstone");
            if (MiscTools.inBounds(blockRedstoneButton.getX(), blockRedstoneButton.getY(), blockRedstoneButton.getWidth(), blockRedstoneButton.getHeight(), mouseX, mouseY)) {
                MutableComponent translatableComponents[] = new MutableComponent[2];
                translatableComponents[0] = Component.translatable("screen.laserio.notblockredstone");
                translatableComponents[1] = Component.translatable("screen.laserio.blockredstone");
                guiGraphics.renderTooltip(font, translatableComponents[currentBlockRedstone ? 1 : 0], mouseX, mouseY);
            }
             */
        }
        if (currentMode == 0 && currentThreshold) {
            Button limitButton = buttons.get("thresholdlimit");
            if (MiscTools.inBounds(limitButton.getX(), limitButton.getY(), limitButton.getWidth(), limitButton.getHeight(), mouseX, mouseY)) {
                guiGraphics.renderTooltip(font, Component.translatable("screen.laserio.thresholdlimit"), mouseX, mouseY);
            }
            Button outputButton = buttons.get("thresholdoutput");
            if (MiscTools.inBounds(outputButton.getX(), outputButton.getY(), outputButton.getWidth(), outputButton.getHeight(), mouseX, mouseY)) {
                guiGraphics.renderTooltip(font, Component.translatable("screen.laserio.thresholdoutput"), mouseX, mouseY);
            }
        }
        if (currentMode == 1) {
            Button strongButton = buttons.get("strong");
            if (MiscTools.inBounds(strongButton.getX(), strongButton.getY(), strongButton.getWidth(), strongButton.getHeight(), mouseX, mouseY)) {
                MutableComponent translatableComponents[] = new MutableComponent[2];
                translatableComponents[0] = Component.translatable("screen.laserio.weak");
                translatableComponents[1] = Component.translatable("screen.laserio.strong");
                guiGraphics.renderTooltip(font, translatableComponents[currentStrong ? 1 : 0], mouseX, mouseY);
            }
        }
        Button channelButton = buttons.get("channel");
        if (MiscTools.inBounds(channelButton.getX(), channelButton.getY(), channelButton.getWidth(), channelButton.getHeight(), mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, Component.translatable("screen.laserio.redstonechannel").append(String.valueOf(currentRedstoneChannel)), mouseX, mouseY);
        }
    }

    public void addModeButton() {
        ResourceLocation[] modeTextures = new ResourceLocation[2];
        modeTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstoneinput.png");
        modeTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstoneoutput.png");
        buttons.put("mode", new ToggleButton(getGuiLeft() + 5, getGuiTop() + 5, 16, 16, modeTextures, currentMode, (button) -> {
            currentMode = CardRedstone.nextTransferMode(card);
            ((ToggleButton) button).setTexturePosition(currentMode);
            modeChange();
        }));
    }

    public void addStrongButton() {
        ResourceLocation[] strongTextures = new ResourceLocation[2];
        strongTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstonelow.png");
        strongTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstonehigh.png");
        buttons.put("strong", new ToggleButton(getGuiLeft() + 5, getGuiTop() + 25, 16, 16, strongTextures, currentStrong ? 1 : 0, (button) -> {
            currentStrong = !currentStrong;
            ((ToggleButton) button).setTexturePosition(currentStrong ? 1 : 0);
        }));
    }

    public void addChannelButton() {
        buttons.put("channel", new ChannelButton(getGuiLeft() + 5, getGuiTop() + 65, 16, 16, currentRedstoneChannel, (button) -> {
            currentRedstoneChannel = CardRedstone.nextRedstoneChannel(card);
            ((ChannelButton) button).setChannel(currentRedstoneChannel);
        }));
    }

    public void addInvertButton() {
        ResourceLocation[] invertTextures = new ResourceLocation[2];
        invertTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/notinvert.png");
        invertTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/invert.png");
        buttons.put("invert", new ToggleButton(getGuiLeft() + 155, getGuiTop() + 5, 16, 16, invertTextures, currentInvert ? 1 : 0, (button) -> {
            currentInvert = !currentInvert;
        ((ToggleButton) button).setTexturePosition(currentInvert ? 1 : 0);
        }));
    }

    public void addThresholdToggleButton() {
        ResourceLocation[] thresholdTextures = new ResourceLocation[2];
        thresholdTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/notthreshold.png");
        thresholdTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/threshold.png");
        buttons.put("thresholdtoggle", new ToggleButton(getGuiLeft() + 155, getGuiTop() + 25, 16, 16, thresholdTextures, currentThreshold ? 1 : 0, (button) -> {
            currentThreshold = !currentThreshold;
            thresholdChange();
            ((ToggleButton) button).setTexturePosition(currentThreshold ? 1 : 0);
        }));
    }

    public void addThresholdLimitButton() {
        buttons.put("thresholdlimit", new NumberButton(getGuiLeft() + 135, getGuiTop() + 25, 16, 16, currentThresholdLimit, (button) -> {
           changeThresholdLimit(-1);
        }));
    }

    public void addThresholdOutputButton() {
        buttons.put("thresholdoutput", new NumberButton(getGuiLeft() + 115, getGuiTop() + 25, 16, 16, currentThresholdOutput, (button) -> {
            changeThresholdOutput(-1);
        }));
    }

    public void addBlockRedstoneButton() {
        ResourceLocation[] bRTextures = new ResourceLocation[2];
        bRTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/notreadblock.png");
        bRTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/readblock.png");
        buttons.put("blockredstone", new ToggleButton(getGuiLeft() + 5, getGuiTop() + 25, 16, 16, bRTextures, currentBlockRedstone ? 1 : 0, (button) -> {
            currentBlockRedstone = !currentBlockRedstone;
            ((ToggleButton) button).setTexturePosition(currentBlockRedstone ? 1 : 0);
        }));
    }

    protected void changeThresholdLimit(int change) {
        if (Screen.hasShiftDown()) change *= 15;
        if (currentMode == 0 && currentThreshold) {
            if (change <= 0) {
                currentThresholdLimit = (byte) (Math.max(currentThresholdLimit + change, 0));
            } else {
                currentThresholdLimit = (byte) (Math.min(currentThresholdLimit + change, 15));
            }}}

    protected void changeThresholdOutput(int change) {
        if (Screen.hasShiftDown()) change *= 15;
        if (currentMode == 0 && currentThreshold) {
        if (change <= 0) {
                currentThresholdOutput = (byte) (Math.max(currentThresholdOutput + change, 0));
            } else {
                currentThresholdOutput = (byte) (Math.min(currentThresholdOutput + change, 15));
            }}}

    protected void setThresholdLimit(NumberButton button, int btn) {
        if (btn == 0)
            changeThresholdLimit(1);
        else if (btn == 1)
            changeThresholdLimit(-1);
        button.setValue(currentThresholdLimit);
        button.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    protected void setThresholdOutput(NumberButton button, int btn) {
        if (btn == 0)
            changeThresholdOutput(1);
        else if (btn == 1)
            changeThresholdOutput(-1);
        button.setValue(currentThresholdOutput);
        button.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    @Override
    public void init() {
        super.init();
        currentMode = CardRedstone.getTransferMode(card);
        currentRedstoneChannel = CardRedstone.getRedstoneChannel(card);
        currentStrong = CardRedstone.getStrong(card);
        currentInvert = CardRedstone.getInvert(card);
        currentThreshold = CardRedstone.getThreshold(card);
        currentThresholdLimit = CardRedstone.getThresholdLimit(card);
        currentThresholdOutput = CardRedstone.getThresholdOutput(card);
        currentBlockRedstone = CardRedstone.getBlockRedstone(card);
        addModeButton();
        addChannelButton();
        addStrongButton();
        addInvertButton();
        addThresholdToggleButton();
        addThresholdLimitButton();
        addThresholdOutputButton();
        //addBlockRedstoneButton();

        if (container.direction != -1) {
            buttons.put("return", new ExtendedButton(getGuiLeft() - 25, getGuiTop() + 1, 25, 20, Component.literal("<--"), (button) -> {
                openNode();
            }));
        }

        for (Map.Entry<String, Button> button : buttons.entrySet()) {
            addRenderableWidget(button.getValue());
        }

        modeChange();
    }

    public void modeChange() {
        Button strongButton = buttons.get("strong");
        Button invertButton = buttons.get("invert");
        Button thresholdToggle = buttons.get("thresholdtoggle");
        Button thresholdLimit = buttons.get("thresholdlimit");
        Button thresholdOutput = buttons.get("thresholdoutput");
        //Button bR = buttons.get("blockredstone");
        if (currentMode == 0) { //Input
            if (!renderables.contains(invertButton))
                addRenderableWidget(invertButton);
            if (!renderables.contains(thresholdToggle))
                addRenderableWidget(thresholdToggle);
            //if (!renderables.contains(bR))
            //    addRenderableWidget(bR);
            removeWidget(strongButton);
            thresholdChange();
        } else if (currentMode == 1) { //output
            if (!renderables.contains(strongButton))
                addRenderableWidget(strongButton);
            if (!renderables.contains(invertButton))
                addRenderableWidget(invertButton);
            if (!renderables.contains(thresholdToggle))
                addRenderableWidget(thresholdToggle);
            removeWidget(invertButton);
            removeWidget(thresholdToggle);
            removeWidget(thresholdLimit);
            removeWidget(thresholdOutput);
            //removeWidget(bR);
        }

    }

    public void thresholdChange() {
        Button thresholdLimit = buttons.get("thresholdlimit");
        Button thresholdOutput = buttons.get("thresholdoutput");
        if (currentThreshold) {
            if (!renderables.contains(thresholdLimit))
                addRenderableWidget(thresholdLimit);
            if (!renderables.contains(thresholdOutput))
                addRenderableWidget(thresholdOutput);
        } else {
            removeWidget(thresholdLimit);
            removeWidget(thresholdOutput);
        }}

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        /*stack.pushPose();
        stack.scale(0.5f, 0.5f, 0.5f);
        if (showExtractAmt()) {
            font.draw(stack, Component.translatable("screen.laserio.extractamt").getString() + ":", 5*2, 45*2, Color.DARK_GRAY.getRGB());
        }
        if (showPriority()) {
            font.draw(stack, Component.translatable("screen.laserio.priority").getString() + ":", 5*2, 50*2, Color.DARK_GRAY.getRGB());
        }
        stack.popPose();*/
        //super.renderLabels(matrixStack, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        saveSettings();
        super.onClose();
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        InputConstants.Key mouseKey = InputConstants.getKey(p_keyPressed_1_, p_keyPressed_2_);
        if (p_keyPressed_1_ == 256 || minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            onClose();

            return true;
        }

        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }


    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    public void saveSettings() {
        PacketHandler.sendToServer(new PacketUpdateRedstoneCard(currentMode, currentRedstoneChannel, currentStrong, currentInvert, currentThreshold, currentThresholdLimit, currentThresholdOutput, currentBlockRedstone));
    }

    public void openNode() {
        saveSettings();
        PacketHandler.sendToServer(new PacketOpenNode(container.sourceContainer, container.direction));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        ChannelButton channelButton = ((ChannelButton) buttons.get("channel"));
        if (MiscTools.inBounds(channelButton.getX(), channelButton.getY(), channelButton.getWidth(), channelButton.getHeight(), x, y)) {
            if (btn == 0)
                currentRedstoneChannel = CardRedstone.nextRedstoneChannel(card);
            else if (btn == 1)
                currentRedstoneChannel = CardRedstone.previousRedstoneChannel(card);
            channelButton.setChannel(currentRedstoneChannel);
            channelButton.playDownSound(Minecraft.getInstance().getSoundManager());
            return true;
        }
        NumberButton thresholdLimitButton = ((NumberButton) buttons.get("thresholdlimit"));
        if (MiscTools.inBounds(thresholdLimitButton.getX(), thresholdLimitButton.getY(), thresholdLimitButton.getWidth(), thresholdLimitButton.getHeight(), x, y)) {
            setThresholdLimit(thresholdLimitButton, btn);
            return true;
        }

        NumberButton thresholdOutputButton = ((NumberButton) buttons.get("thresholdoutput"));
        if (MiscTools.inBounds(thresholdOutputButton.getX(), thresholdOutputButton.getY(), thresholdOutputButton.getWidth(), thresholdOutputButton.getHeight(), x, y)) {
            setThresholdOutput(thresholdOutputButton, btn);
            return true;
        }

        return super.mouseClicked(x, y, btn);
    }
}