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
    protected boolean currentThreshold;
    protected byte currentThresholdLimit;
    protected byte currentThresholdOutput;
    protected boolean currentStrong;
    protected byte currentOutputMode;
    protected byte currentLogicOperation;
    protected byte currentLogicOperationChannel;
    //protected byte currentSpecialFeature;
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
            Button thresholdToggleButton = buttons.get("thresholdToggle");
            if (MiscTools.inBounds(thresholdToggleButton.getX(), thresholdToggleButton.getY(), thresholdToggleButton.getWidth(), thresholdToggleButton.getHeight(), mouseX, mouseY)) {
                guiGraphics.renderTooltip(font, Component.translatable("screen.laserio.redstone.threshold"), mouseX, mouseY);
            }
            if (currentThreshold) {
                Button thresholdLimitButton = buttons.get("thresholdLimit");
                if (MiscTools.inBounds(thresholdLimitButton.getX(), thresholdLimitButton.getY(), thresholdLimitButton.getWidth(), thresholdLimitButton.getHeight(), mouseX, mouseY)) {
                    guiGraphics.renderTooltip(font, Component.translatable("screen.laserio.redstone.thresholdlimit"), mouseX, mouseY);
                }
                Button thresholdOutputButton = buttons.get("thresholdOutput");
                if (MiscTools.inBounds(thresholdOutputButton.getX(), thresholdOutputButton.getY(), thresholdOutputButton.getWidth(), thresholdOutputButton.getHeight(), mouseX, mouseY)) {
                    guiGraphics.renderTooltip(font, Component.translatable("screen.laserio.redstone.thresholdoutput"), mouseX, mouseY);
                }
            }
        } else {
            Button strongButton = buttons.get("strong");
            if (MiscTools.inBounds(strongButton.getX(), strongButton.getY(), strongButton.getWidth(), strongButton.getHeight(), mouseX, mouseY)) {
                MutableComponent translatableComponents[] = new MutableComponent[2];
                translatableComponents[0] = Component.translatable("screen.laserio.weak");
                translatableComponents[1] = Component.translatable("screen.laserio.strong");
                guiGraphics.renderTooltip(font, translatableComponents[currentStrong ? 1 : 0], mouseX, mouseY);
            }
            Button outputModeButton = buttons.get("outputMode");
            if (MiscTools.inBounds(outputModeButton.getX(), outputModeButton.getY(), outputModeButton.getWidth(), outputModeButton.getHeight(), mouseX, mouseY)) {
                MutableComponent translatableComponents[] = new MutableComponent[3];
                translatableComponents[0] = Component.translatable("screen.laserio.redstone.normal");
                translatableComponents[1] = Component.translatable("screen.laserio.redstone.complementary");
                translatableComponents[2] = Component.translatable("screen.laserio.redstone.not");
                guiGraphics.renderTooltip(font, translatableComponents[currentOutputMode], mouseX, mouseY);
            }
            Button logicOperationButton = buttons.get("logicOperation");
            if (MiscTools.inBounds(logicOperationButton.getX(), logicOperationButton.getY(), logicOperationButton.getWidth(), logicOperationButton.getHeight(), mouseX, mouseY)) {
                MutableComponent translatableComponents[] = new MutableComponent[4];
                translatableComponents[0] = Component.translatable("screen.laserio.redstone.nologicoperation");
                translatableComponents[1] = Component.translatable("screen.laserio.redstone.or");
                translatableComponents[2] = Component.translatable("screen.laserio.redstone.and");
                translatableComponents[3] = Component.translatable("screen.laserio.redstone.xor");
                guiGraphics.renderTooltip(font, translatableComponents[currentLogicOperation], mouseX, mouseY);
            }
            if (currentLogicOperation != 0) {
                Button logicOperationChannelButton = buttons.get("logicOperationChannel");
                if (MiscTools.inBounds(logicOperationChannelButton.getX(), logicOperationChannelButton.getY(), logicOperationChannelButton.getWidth(), logicOperationChannelButton.getHeight(), mouseX, mouseY)) {
                    guiGraphics.renderTooltip(font, Component.translatable("screen.laserio.redstonechannel").append(String.valueOf(currentLogicOperationChannel)), mouseX, mouseY);
                }
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

    public void addChannelButton() {
        buttons.put("channel", new ChannelButton(getGuiLeft() + 5, getGuiTop() + 65, 16, 16, currentRedstoneChannel, (button) -> {
            currentRedstoneChannel = CardRedstone.nextRedstoneChannel(card);
            ((ChannelButton) button).setChannel(currentRedstoneChannel);
        }));
    }

    public void addThresholdToggleButton() {
        ResourceLocation[] thresholdTextures = new ResourceLocation[2];
        thresholdTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstone_thresholdfalse.png");
        thresholdTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstone_thresholdtrue.png");
        buttons.put("thresholdToggle", new ToggleButton(getGuiLeft() + 5, getGuiTop() + 25, 16, 16, thresholdTextures, currentThreshold ? 1 : 0, (button) -> {
            currentThreshold = !currentThreshold;
            ((ToggleButton) button).setTexturePosition(currentThreshold ? 1 : 0);
            thresholdChange();
        }));
    }

    public void addThresholdLimitButton() {
        buttons.put("thresholdLimit", new NumberButton(getGuiLeft() + 25, getGuiTop() + 25, 16, 16, currentThresholdLimit, (button) -> {
           changeThresholdLimit(-1);
        }));
    }

    public void addThresholdOutputButton() {
        buttons.put("thresholdOutput", new NumberButton(getGuiLeft() + 45, getGuiTop() + 25, 16, 16, currentThresholdOutput, (button) -> {
            changeThresholdOutput(-1);
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

    public void addOutputModeButton() {
        ResourceLocation[] outputModeTextures = new ResourceLocation[3];
        outputModeTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstone_normal.png");
        outputModeTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstone_complementary.png");
        outputModeTextures[2] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstone_not.png");
        buttons.put("outputMode", new ToggleButton(getGuiLeft() + 155, getGuiTop() + 5, 16, 16, outputModeTextures, currentOutputMode, (button) -> {
            currentOutputMode = (byte) (currentOutputMode == 2 ? 0 : currentOutputMode + 1);
            ((ToggleButton) button).setTexturePosition(currentOutputMode);
        }));
    }

    public void addLogicOperationButton() {
        ResourceLocation[] logicOperationTextures = new ResourceLocation[4];
        logicOperationTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstone_nologicoperation.png");
        logicOperationTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstone_or.png");
        logicOperationTextures[2] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstone_and.png");
        logicOperationTextures[3] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/redstone_xor.png");
        buttons.put("logicOperation", new ToggleButton(getGuiLeft() + 155, getGuiTop() + 25, 16, 16, logicOperationTextures, currentLogicOperation, (button) -> {
            currentLogicOperation = (byte) (currentLogicOperation == 3 ? 0 : currentLogicOperation + 1);
            ((ToggleButton) button).setTexturePosition(currentLogicOperation);
            logicOperationChange();
        }));
    }

    public void addLogicOperationChannelButton() {
        buttons.put("logicOperationChannel", new ChannelButton(getGuiLeft() + 135, getGuiTop() + 25, 16, 16, currentLogicOperationChannel, (button) -> {
            currentLogicOperationChannel = CardRedstone.nextRedstoneChannelOperation(card);
            ((ChannelButton) button).setChannel(currentLogicOperationChannel);
        }));
    }
    /*
    public void addSpecialFeatureButton() {
        ResourceLocation[] specialFeatureTextures = new ResourceLocation[2];
        specialFeatureTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/add.png");
        specialFeatureTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/remove.png");
        buttons.put("specialFeature", new ToggleButton(getGuiLeft() + 155, getGuiTop() + 45, 16, 16, specialFeatureTextures, currentSpecialFeature, (button) -> {
            currentSpecialFeature = (byte) (currentSpecialFeature == 1 ? 0 : currentSpecialFeature + 1);
            ((ToggleButton) button).setTexturePosition(currentSpecialFeature);
        }));
    }
    */
    @Override
    public void init() {
        super.init();
        currentMode = CardRedstone.getTransferMode(card);
        currentRedstoneChannel = CardRedstone.getRedstoneChannel(card);
        currentThreshold = CardRedstone.getThreshold(card);
        currentThresholdLimit = CardRedstone.getThresholdLimit(card);
        currentThresholdOutput = CardRedstone.getThresholdOutput(card);
        currentStrong = CardRedstone.getStrong(card);
        currentOutputMode = CardRedstone.getOutputMode(card);
        currentLogicOperation = CardRedstone.getLogicOperation(card);
        currentLogicOperationChannel = CardRedstone.getRedstoneChannelOperation(card);
        //currentSpecialFeature = CardRedstone.getSpecialFeature(card);
        addModeButton();
        addChannelButton();
        addThresholdToggleButton();
        addThresholdLimitButton();
        addThresholdOutputButton();
        addStrongButton();
        addOutputModeButton();
        addLogicOperationButton();
        addLogicOperationChannelButton();
        //addSpecialFeatureButton();

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
        Button outputModeButton = buttons.get("outputMode");
        Button thresholdToggleButton = buttons.get("thresholdToggle");
        Button thresholdLimitButton = buttons.get("thresholdLimit");
        Button thresholdOutputButton = buttons.get("thresholdOutput");
        Button logicOperationButton = buttons.get("logicOperation");
        Button logicOperationChannelButton = buttons.get("logicOperationChannel");
        //Button specialFeatureButton = buttons.get("specialFeature");
        if (currentMode == 0) { //input
            if (!renderables.contains(thresholdToggleButton))
                addRenderableWidget(thresholdToggleButton);
            removeWidget(strongButton);
            removeWidget(outputModeButton);
            removeWidget(logicOperationButton);
            removeWidget(logicOperationChannelButton);
            //removeWidget(specialFeatureButton);
            thresholdChange();
        } else { //output
            if (!renderables.contains(strongButton))
                addRenderableWidget(strongButton);
            if (!renderables.contains(outputModeButton))
                addRenderableWidget(outputModeButton);
            if (!renderables.contains(logicOperationButton))
                addRenderableWidget(logicOperationButton);
            if (!renderables.contains(logicOperationChannelButton))
                addRenderableWidget(logicOperationChannelButton);
            //if (!renderables.contains(specialFeatureButton))
            //    addRenderableWidget(specialFeatureButton);
            removeWidget(thresholdToggleButton);
            removeWidget(thresholdLimitButton);
            removeWidget(thresholdOutputButton);
            logicOperationChange();
        }
    }

    public void thresholdChange() {
        Button thresholdLimitButton = buttons.get("thresholdLimit");
        Button thresholdOutputButton = buttons.get("thresholdOutput");
        if (currentThreshold) {
            if (!renderables.contains(thresholdLimitButton))
                addRenderableWidget(thresholdLimitButton);
            if (!renderables.contains(thresholdOutputButton))
                addRenderableWidget(thresholdOutputButton);
        } else {
            removeWidget(thresholdLimitButton);
            removeWidget(thresholdOutputButton);
        }
    }

    public void logicOperationChange() {
        Button logicOperationChannelButton = buttons.get("logicOperationChannel");
        if (currentLogicOperation != 0) {
            if (!renderables.contains(logicOperationChannelButton))
                addRenderableWidget(logicOperationChannelButton);
        } else {
            removeWidget(logicOperationChannelButton);
        }
    }

    public void changeThresholdLimit(int change) {
        if (Screen.hasShiftDown()) change *= 15;
        if (change < 0) {
            currentThresholdLimit = (byte) (Math.max(currentThresholdLimit + change, 0));
        } else {
            currentThresholdLimit = (byte) (Math.min(currentThresholdLimit + change, 15));
        }
    }

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
        PacketHandler.sendToServer(new PacketUpdateRedstoneCard(currentMode, currentRedstoneChannel, currentThreshold, currentThresholdLimit, currentThresholdOutput, currentStrong, currentOutputMode, currentLogicOperation, currentLogicOperationChannel));
    }

    public void openNode() {
        saveSettings();
        PacketHandler.sendToServer(new PacketOpenNode(container.sourceContainer, container.direction));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public void changeThresholdOutput(int change) {
        if (Screen.hasShiftDown()) change *= 15;
        if (change < 0) {
            currentThresholdOutput = (byte) (Math.max(currentThresholdOutput + change, 0));
        } else {
            currentThresholdOutput = (byte) (Math.min(currentThresholdOutput + change, 15));
        }
    }

    public void setThresholdLimit(NumberButton button, int btn) {
        if (btn == 0)
            changeThresholdLimit(1);
        else if (btn == 1)
            changeThresholdLimit(-1);
        button.setValue(currentThresholdLimit);
        button.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    public void setThresholdOutput(NumberButton button, int btn) {
        if (btn == 0)
            changeThresholdOutput(1);
        else if (btn == 1)
            changeThresholdOutput(-1);
        button.setValue(currentThresholdOutput);
        button.playDownSound(Minecraft.getInstance().getSoundManager());
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
        NumberButton thresholdLimitButton = ((NumberButton) buttons.get("thresholdLimit"));
        if (currentMode == 0 && currentThreshold && MiscTools.inBounds(thresholdLimitButton.getX(), thresholdLimitButton.getY(), thresholdLimitButton.getWidth(), thresholdLimitButton.getHeight(), x, y)) {
            setThresholdLimit(thresholdLimitButton, btn);
            return true;
        }
        NumberButton thresholdOutputButton = ((NumberButton) buttons.get("thresholdOutput"));
        if (currentMode == 0 && currentThreshold && MiscTools.inBounds(thresholdOutputButton.getX(), thresholdOutputButton.getY(), thresholdOutputButton.getWidth(), thresholdOutputButton.getHeight(), x, y)) {
            setThresholdOutput(thresholdOutputButton, btn);
            return true;
        }
        ChannelButton logicOperationChannelButton = ((ChannelButton) buttons.get("logicOperationChannel"));
        if (currentMode == 1 && currentLogicOperation != 0 && MiscTools.inBounds(logicOperationChannelButton.getX(), logicOperationChannelButton.getY(), logicOperationChannelButton.getWidth(), logicOperationChannelButton.getHeight(), x, y)) {
            if (btn == 0)
                currentLogicOperationChannel = CardRedstone.nextRedstoneChannelOperation(card);
            else if (btn == 1)
                currentLogicOperationChannel = CardRedstone.previousRedstoneChannelOperation(card);
            logicOperationChannelButton.setChannel(currentLogicOperationChannel);
            logicOperationChannelButton.playDownSound(Minecraft.getInstance().getSoundManager());
            return true;
        }
        return super.mouseClicked(x, y, btn);
    }
}