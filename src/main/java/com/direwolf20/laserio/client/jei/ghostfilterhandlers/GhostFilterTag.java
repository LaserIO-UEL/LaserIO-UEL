package com.direwolf20.laserio.client.jei.ghostfilterhandlers;

import com.direwolf20.laserio.client.screens.FilterTagScreen;
import com.direwolf20.laserio.util.JEIIntegrationUtil;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;

import java.util.List;

public class GhostFilterTag implements IGhostIngredientHandler<FilterTagScreen> {
    @Override
    public <I> List<Target<I>> getTargetsTyped(FilterTagScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        return JEIIntegrationUtil.getTargetsTyped(gui, ingredient, doStart);
    }

    @Override
    public void onComplete() {
        // NO OP
    }
}