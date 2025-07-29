package com.direwolf20.laserio.integration.mekanism.client.screens;

import com.direwolf20.laserio.client.screens.CardItemScreen;
import com.direwolf20.laserio.client.screens.widgets.NumberButton;
import com.direwolf20.laserio.client.screens.widgets.ToggleButton;
import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.containers.CardItemContainer;
import com.direwolf20.laserio.common.containers.customslot.FilterBasicSlot;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.filters.FilterCount;
import com.direwolf20.laserio.common.network.PacketHandler;
import com.direwolf20.laserio.common.network.packets.PacketGhostSlot;
import com.direwolf20.laserio.common.network.packets.PacketOpenNode;
import com.direwolf20.laserio.common.network.packets.PacketUpdateCard;
import com.direwolf20.laserio.common.network.packets.PacketUpdateFilter;
import com.direwolf20.laserio.integration.mekanism.common.items.cards.CardChemical;
import com.direwolf20.laserio.integration.mekanism.util.MekanismStatics;
import com.direwolf20.laserio.setup.Config;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CardChemicalScreen extends CardItemScreen {
    private int currentChemicalExtractAmt;
    protected final int filterStartX;
    protected final int filterStartY;
    protected final int filterEndX;
    protected final int filterEndY;

    public CardChemicalScreen(CardItemContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        filterStartX = 35;
        filterStartY = 16;
        filterEndX = 125;
        filterEndY = 70;
    }

    @Override
    public void init() {
        this.currentTicks = CardChemical.getExtractSpeed(card);
        this.currentChemicalExtractAmt = CardChemical.getChemicalExtractAmt(card);
        super.init();
        this.renderChemicals = true;
    }

    @Override
    public void addAmtButton() {
        buttons.put("amount", new NumberButton(this.leftPos + 139, this.topPos + 25, 32, 12, currentMode == 0 ? currentPriority : currentChemicalExtractAmt, (button) -> {
            changeAmount(-1);
        }));
    }

    @Override
    public void addModeButton() {
        ResourceLocation[] modeTextures = {
                new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/modeinserter.png"),
                new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/modeextractor.png"),
                new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/modestocker.png"),
                new ResourceLocation(LaserIO.MODID, "textures/gui/buttons/modesensor.png")
        };
        buttons.put("mode", new ToggleButton(this.leftPos + 5, this.topPos + 5, 16, 16, modeTextures, currentMode, (button) -> {
            currentMode = BaseCard.nextTransferMode(card);
            ((ToggleButton) button).setTexturePosition(currentMode);
            ((NumberButton) buttons.get("amount")).setValue(currentMode == 0 ? currentPriority : currentChemicalExtractAmt);
            modeChange();
        }));
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack itemStack = this.hoveredSlot.getItem();
            if (hoveredSlot instanceof FilterBasicSlot) {
                ChemicalStack<?> chemicalStack = MekanismStatics.getFirstChemicalOnItemStack(itemStack);
                if (chemicalStack.isEmpty())
                    pGuiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemStack), itemStack.getTooltipImage(), itemStack, pX, pY);
                else
                    pGuiGraphics.renderTooltip(this.font, chemicalStack.getTextComponent(), pX, pY);
                return;
            }
            pGuiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemStack), itemStack.getTooltipImage(), itemStack, pX, pY);
        }
    }

    @Override
    public void changeAmount(int change) {
        if (Screen.hasShiftDown()) change *= 10;
        if (Screen.hasControlDown()) change *= 100;
        if (Screen.hasAltDown()) change *= 1000;
        int overclockerCount = container.getSlot(1).getItem().getCount();
        if (change < 0) {
            if (currentMode == 0) {
                currentPriority = (short) (Math.max(currentPriority + change, -4096));
            } else {
                currentChemicalExtractAmt = (Math.max(currentChemicalExtractAmt + change, 1));
            }
        } else {
            if (currentMode == 0) {
                currentPriority = (short) (Math.min(currentPriority + change, 4096));
            } else {
                currentChemicalExtractAmt = (Math.min(currentChemicalExtractAmt + change, Math.max(overclockerCount * Config.MULTIPLIER_MILLI_BUCKETS_CHEMICAL.get(), Config.BASE_MILLI_BUCKETS_CHEMICAL.get())));
            }
        }
    }

    @Override
    public void changeTick(int change) {
        if (Screen.hasShiftDown()) change *= 10;
        if (Screen.hasControlDown()) change *= 100;
        if (change < 0) {
            currentTicks = (Math.max(currentTicks + change, Config.MIN_TICKS_CHEMICAL.get().get(container.getSlot(1).getItem().getCount())));
        } else {
            currentTicks = (Math.min(currentTicks + change, 1200));
        }
    }

    @Override
    public boolean filterSlot(int btn, boolean isScrollWheel) {
        ItemStack slotStack = hoveredSlot.getItem();
        if (!MekanismStatics.doesItemStackHoldChemicals(slotStack))
            return super.filterSlot(btn, isScrollWheel);
        if (slotStack.isEmpty()) return true;
        if (btn == 2) {
            slotStack.setCount(0);
            PacketHandler.sendToServer(new PacketGhostSlot(hoveredSlot.index, slotStack, slotStack.getCount(), 0));
            return true;
        }
        int amt = (btn == 0) ? 1 : -1;
        int filterSlot = hoveredSlot.index - CardItemContainer.SLOTS;
        int currentMBAmt = FilterCount.getSlotAmount(filter, filterSlot);
        if (Screen.hasShiftDown()) amt *= 10;
        if (Screen.hasControlDown()) amt *= 100;
        if (Screen.hasAltDown()) amt *= 1000;
        int newMBAmt = currentMBAmt + amt;
        if (newMBAmt > 4096000) newMBAmt = 4096000;
        if (newMBAmt <= 0) newMBAmt = (isScrollWheel) ? 1 : 0;

        PacketHandler.sendToServer(new PacketGhostSlot(hoveredSlot.index, slotStack, slotStack.getCount(), newMBAmt));
        return true;
    }

    @Override
    public void setExtract(NumberButton amountButton, int btn) {
        if (btn == 0)
            changeAmount(1);
        else if (btn == 1)
            changeAmount(-1);
        amountButton.setValue(currentMode == 0 ? currentPriority : currentChemicalExtractAmt);
        amountButton.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    @Override
    public void openNode() {
        saveSettings();
        PacketHandler.sendToServer(new PacketOpenNode(container.sourceContainer, container.direction));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void saveSettings() {
        if (showFilter)
            PacketHandler.sendToServer(new PacketUpdateFilter(isAllowList == 1, isCompareNBT == 1));
        PacketHandler.sendToServer(new PacketUpdateCard(currentMode, currentChannel, currentChemicalExtractAmt, currentPriority, currentSneaky, (short) currentTicks, currentExact, currentRegulate, (byte) currentRoundRobin, 0, 0, currentRedstoneMode, currentRedstoneChannel, currentAndMode));
    }

    @Override
    protected void slotClicked(Slot slot, int inventorySlotIndex, int depositedAmount, ClickType clickType) {
        super.slotClicked(slot, inventorySlotIndex, depositedAmount, clickType);

        int newOverclockerCount = container.getSlot(1).getItem().getCount();
        if (newOverclockerCount == lastOverclockerCount) {
            return;
        }

        currentChemicalExtractAmt = Math.max(newOverclockerCount * Config.MULTIPLIER_MILLI_BUCKETS_CHEMICAL.get(), Config.BASE_MILLI_BUCKETS_CHEMICAL.get());
        lastOverclockerCount = newOverclockerCount;
        if (currentMode != 0) {
            ((NumberButton) buttons.get("amount")).setValue(currentChemicalExtractAmt);
        }
    }
}