package com.direwolf20.laserio.client.jei.ghostfilterhandlers;

import com.direwolf20.laserio.client.screens.FilterNBTScreen;
import com.direwolf20.laserio.util.JEIIntegrationUtil;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;

import java.util.List;

public class GhostFilterNBT implements IGhostIngredientHandler<FilterNBTScreen> {
    @Override
    public <I> List<Target<I>> getTargetsTyped(FilterNBTScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        return JEIIntegrationUtil.getTargetsTyped(gui, ingredient, doStart);
    }

    @Override
    public void onComplete() {
        // NO OP
    }
}