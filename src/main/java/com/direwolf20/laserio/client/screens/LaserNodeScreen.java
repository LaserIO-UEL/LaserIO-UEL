package com.direwolf20.laserio.client.screens;

import com.direwolf20.laserio.client.screens.widgets.IconButton;
import com.direwolf20.laserio.client.screens.widgets.ToggleButton;
import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.containers.CardHolderContainer;
import com.direwolf20.laserio.common.containers.LaserNodeContainer;
import com.direwolf20.laserio.common.containers.customslot.CardHolderSlot;
import com.direwolf20.laserio.common.containers.customslot.LaserNodeSlot;
import com.direwolf20.laserio.common.items.CardCloner;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.network.PacketHandler;
import com.direwolf20.laserio.common.network.packets.PacketCopyPasteCard;
import com.direwolf20.laserio.common.network.packets.PacketOpenCard;
import com.direwolf20.laserio.common.network.packets.PacketOpenNode;
import com.direwolf20.laserio.common.network.packets.PacketToggleParticles;
import com.direwolf20.laserio.util.MiscTools;
import com.direwolf20.laserio.util.Vec2i;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class LaserNodeScreen extends AbstractContainerScreen<LaserNodeContainer> {
    private static final ResourceLocation GUI = new ResourceLocation(LaserIO.MODID, "textures/gui/laser_node.png");
    private static final MutableComponent[] SIDES = {
            Component.translatable("screen.laserio.down"),
            Component.translatable("screen.laserio.up"),
            Component.translatable("screen.laserio.north"),
            Component.translatable("screen.laserio.south"),
            Component.translatable("screen.laserio.west"),
            Component.translatable("screen.laserio.east"),
    };
    //In the final GUI the "Up" tab is rendered before the "Down" one
    //from left to right, however these two tabs are flipped in this
    //array because like that we follow the Direction enum order.
    //Thanks to that, we can use Direction's ordinal values to get
    //the corresponding tab
    public static final Vec2i[] TABS = {
            new Vec2i(34, 4), //Down
            new Vec2i(6, 4), //Up
            new Vec2i(62, 4), //North
            new Vec2i(90, 4), //South
            new Vec2i(118, 4), //West
            new Vec2i(146, 4) //East
    };
    protected final LaserNodeContainer container;
    private boolean showCardHolderUI;
    private boolean currentParticles;
    private Button settingsButton;
    private Button particlesButton;

    public LaserNodeScreen(LaserNodeContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.container = container;
        this.imageHeight = 181;
        showCardHolderUI = !container.cardHolder.isEmpty();
        this.currentParticles = container.tile.getShowParticles();
    }

    @Override
    public void init() {
        super.init();
        List<AbstractWidget> leftWidgets = new ArrayList<>();
        ResourceLocation settings = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/settings.png");
        settingsButton = new IconButton(getGuiLeft() + 155, getGuiTop() + 25, 16, 16, settings, (button) -> {
            Minecraft.getInstance().setScreen(new LaserNodeSettingsScreen(container, Component.translatable("screen.laserio.settings")));
        });
        leftWidgets.add(settingsButton);

        ResourceLocation[] regulateTextures = new ResourceLocation[2];
        regulateTextures[0] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/regulatefalse.png");
        regulateTextures[1] = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/regulatetrue.png");
        particlesButton = new ToggleButton(getGuiLeft() + 155, getGuiTop() + 45, 16, 16, regulateTextures, currentParticles ? 1 : 0, (button) -> {
            currentParticles = !currentParticles;
            ((ToggleButton) button).setTexturePosition(currentParticles ? 1 : 0);
            PacketHandler.sendToServer(new PacketToggleParticles(currentParticles));
        });
        leftWidgets.add(particlesButton);

        for (int i = 0; i < leftWidgets.size(); i++) {
            addRenderableWidget(leftWidgets.get(i));
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        if (showCardHolderUI)
            return mouseX < (double) guiLeftIn - 100 || mouseY < (double) guiTopIn || mouseX >= (double) (guiLeftIn + this.imageWidth) || mouseY >= (double) (guiTopIn + this.imageHeight);
        return super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn, mouseButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        toggleHolderSlots();
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (MiscTools.inBounds(particlesButton.getX(), particlesButton.getY(), particlesButton.getWidth(), particlesButton.getHeight(), mouseX, mouseY)) {
            MutableComponent translatableComponents[] = new MutableComponent[2];
            translatableComponents[0] = Component.translatable("screen.laserio.showparticles");
            translatableComponents[1] = Component.translatable("screen.laserio.hideparticles");
            guiGraphics.renderTooltip(font, currentParticles ? translatableComponents[0] : translatableComponents[1], mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Vec2i tab = TABS[container.side];
        guiGraphics.fill(tab.x + 2, tab.y + 2, tab.x + 22, tab.y + 14, 0xFFC6C6C6);
        guiGraphics.fill(tab.x, tab.y + 11, tab.x + 2, tab.y + 12, 0xFFFFFFFF);
        guiGraphics.fill(tab.x + 22, tab.y + 11, tab.x + 24, tab.y + 12, 0xFFFFFFFF);
        String side = SIDES[container.side].getString();
        int color = Color.DARK_GRAY.getRGB();
        guiGraphics.drawString(font, side, imageWidth / 2 - font.width(side) / 2, 20, color, false);
        guiGraphics.drawString(font, "U", 15, 7, color, false);
        guiGraphics.drawString(font, "D", 43, 7, color, false);
        guiGraphics.drawString(font, "N", 71, 7, color, false);
        guiGraphics.drawString(font, "S", 99, 7, color, false);
        guiGraphics.drawString(font, "W", 128, 7, color, false);
        guiGraphics.drawString(font, "E", 155, 7, color, false);
        for (Direction direction : Direction.values()) {
            ItemStack itemStack = getAdjacentBlock(direction);
            if (!itemStack.isEmpty()) {
                tab = TABS[direction.ordinal()];
                guiGraphics.renderItem(itemStack, tab.x + 4, tab.y - 14, 0);
                if (MiscTools.inBounds(getGuiLeft() + tab.x + 4, getGuiTop() + tab.y - 14, 16, 16, mouseX, mouseY)) {
                    guiGraphics.renderTooltip(font, itemStack, mouseX - getGuiLeft(), mouseY - getGuiTop());
                }
            }
        }
    }

    protected ItemStack getAdjacentBlock(Direction direction) {
        BlockState blockState = container.playerEntity.level().getBlockState(this.container.tile.getBlockPos().relative(direction));
        ItemStack itemStack = blockState.getBlock().asItem().getDefaultInstance();
        return itemStack;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
        if (showCardHolderUI) {
            ResourceLocation CardHolderGUI = new ResourceLocation(LaserIO.MODID, "textures/gui/cardholder_node.png");
            RenderSystem.setShaderTexture(0, CardHolderGUI);
            guiGraphics.blit(CardHolderGUI, getGuiLeft() - 100, getGuiTop() + 24, 0, 0, this.imageWidth, this.imageHeight);
        }
    }

    public void toggleHolderSlots() {
        for (int i = LaserNodeContainer.CARDSLOTS; i < (LaserNodeContainer.CARDSLOTS + CardHolderContainer.SLOTS); i++) {
            if (i >= container.slots.size()) continue;
            Slot slot = container.getSlot(i);
            if (slot instanceof CardHolderSlot cardHolderSlot) {
                cardHolderSlot.setEnabled(showCardHolderUI);
            }
        }
    }

    private void openTab(byte tabIndex) {
        PacketHandler.sendToServer(new PacketOpenNode(container.tile.getBlockPos(), tabIndex));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        byte tabIndex;
        if (delta > 0) {
            tabIndex = switch(container.side) {
                case 1 -> 0; //Down -> Up
                case 0 -> 2; //Up -> North
                default -> (byte) (container.side + 1); //Next tab
            };
        } else {
            tabIndex = switch(container.side) {
                default -> (byte) (container.side - 1); //Previous tab
                case 2 -> 0; //North -> Up
                case 0 -> 1; //Up -> Down
                case 1 -> -1; //No more tabs
            };
        }
        if (tabIndex >= 0 && tabIndex < TABS.length) {
            openTab(tabIndex);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (hoveredSlot != null && container.getCarried().getItem() instanceof CardCloner) {
            if (hoveredSlot instanceof LaserNodeSlot && !hoveredSlot.getItem().isEmpty()) {
                if (btn == 0) //Left click
                    PacketHandler.sendToServer(new PacketCopyPasteCard(hoveredSlot.getSlotIndex(), true));
                else if (btn == 1) //Right click
                    PacketHandler.sendToServer(new PacketCopyPasteCard(hoveredSlot.getSlotIndex(), false));
            }
            return true;
        }
        for (byte i = 0; i < TABS.length; i++) {
            Vec2i tab = TABS[i];
            if (MiscTools.inBounds(getGuiLeft() + tab.x, getGuiTop() + tab.y, 24, 12, x, y) && container.side != i) {
                openTab(i);
                return true;
            }
        }
        if (hoveredSlot == null || hoveredSlot.getItem().isEmpty() || !(hoveredSlot.getItem().getItem() instanceof BaseCard)) {
            return super.mouseClicked(x, y, btn);
        }
        if (btn == 1 && hoveredSlot instanceof LaserNodeSlot) { //Right click
            int slot = hoveredSlot.getSlotIndex();
            PacketHandler.sendToServer(new PacketOpenCard(slot, container.tile.getBlockPos(), hasShiftDown()));
            return true;
        }
        return super.mouseClicked(x, y, btn);
    }
}