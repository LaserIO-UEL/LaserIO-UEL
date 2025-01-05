package com.direwolf20.laserio.client.jei.ghostfilterhandlers;

import com.direwolf20.laserio.client.screens.CardItemScreen;
import com.direwolf20.laserio.util.JEIIntegrationUtil;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;

import java.util.List;

public class GhostFilterCard implements IGhostIngredientHandler<CardItemScreen> {
    @Override
    public <I> List<Target<I>> getTargetsTyped(CardItemScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        return JEIIntegrationUtil.getTargetsTyped(gui, ingredient, doStart);
    }

    @Override
    public void onComplete() {
        // NO OP
    }
}
