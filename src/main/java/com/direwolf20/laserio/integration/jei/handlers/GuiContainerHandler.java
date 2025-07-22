package com.direwolf20.laserio.integration.jei.handlers;

import com.direwolf20.laserio.client.screens.CardEnergyScreen;
import com.direwolf20.laserio.client.screens.CardItemScreen;
import com.direwolf20.laserio.client.screens.LaserNodeScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;

import java.util.ArrayList;
import java.util.List;

public class GuiContainerHandler<T extends AbstractContainerScreen<?>> implements IGuiContainerHandler<T> {
    @Override
    public List<Rect2i> getGuiExtraAreas(T containerScreen) {
        List<Rect2i> extraAreas = new ArrayList<>();
        boolean showCardHolderUI = false;

        if (containerScreen instanceof LaserNodeScreen nodeScreen) {
            showCardHolderUI = nodeScreen.isCardHolderUIShown();
        } else if (containerScreen instanceof CardItemScreen cardItemScreen) {
            showCardHolderUI = cardItemScreen.isCardHolderUIShown();
        } else if (containerScreen instanceof CardEnergyScreen cardEnergyScreen) {
            showCardHolderUI = cardEnergyScreen.isCardHolderUIShown();
        }

        if (showCardHolderUI) {
            Rect2i cardHolderArea = new Rect2i(containerScreen.getGuiLeft() - 100, containerScreen.getGuiTop() + 24, 100, 68);
            extraAreas.add(cardHolderArea);
        }

        return extraAreas;
    }
}