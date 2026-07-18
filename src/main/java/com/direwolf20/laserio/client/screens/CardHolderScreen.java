package com.direwolf20.laserio.client.screens;

import com.direwolf20.laserio.client.screens.widgets.IconButton;
import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.containers.CardHolderContainer;
import com.direwolf20.laserio.common.containers.customslot.CardHolderSlot;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.filters.BaseFilter;
import com.direwolf20.laserio.common.network.PacketHandler;
import com.direwolf20.laserio.common.network.packets.PacketClearCards;
import com.direwolf20.laserio.common.network.packets.PacketOpenCard;
import com.direwolf20.laserio.common.network.packets.PacketOpenFilter;
import com.direwolf20.laserio.util.MiscTools;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class CardHolderScreen extends AbstractContainerScreen<CardHolderContainer> {
    private static final ResourceLocation GUI = new ResourceLocation(LaserIO.MODID, "textures/gui/cardholder.png");
    private Button clearButton;

    public CardHolderScreen(CardHolderContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.imageHeight = 181;
    }

    @Override
    public void init() {
        super.init();

        ResourceLocation texture = new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/matchnbtfalse.png");
        clearButton = new IconButton(this.leftPos + 155, this.topPos + 15, 16, 16, texture, (button) -> {
            PacketHandler.sendToServer(new PacketClearCards());
        });

        addRenderableWidget(clearButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        if (MiscTools.inBounds(clearButton.getX(), clearButton.getY(), clearButton.getWidth(), clearButton.getHeight(), mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, Component.translatable("screen.laserio.clearcards"), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        //super.renderLabels(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        guiGraphics.blit(GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (btn == 1 && hoveredSlot instanceof CardHolderSlot) { //Right click
            int slot = hoveredSlot.getSlotIndex();
            if (hoveredSlot.hasItem() && hoveredSlot.getItem().getItem() instanceof BaseFilter) {
                PacketHandler.sendToServer(new PacketOpenFilter(slot));
            } else {
                PacketHandler.sendToServer(new PacketOpenCard(slot, new BlockPos(0, -1000, 0), false));
            }
            return true;
        }
        return super.mouseClicked(x, y, btn);
    }
}