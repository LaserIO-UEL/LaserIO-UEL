package com.direwolf20.laserio.client.jei;

import com.direwolf20.laserio.client.screens.CardEnergyScreen;
import com.direwolf20.laserio.client.screens.CardItemScreen;
import com.direwolf20.laserio.client.screens.FilterBasicScreen;
import com.direwolf20.laserio.client.screens.FilterCountScreen;
import com.direwolf20.laserio.client.screens.FilterNBTScreen;
import com.direwolf20.laserio.client.screens.FilterTagScreen;
import com.direwolf20.laserio.client.screens.LaserNodeScreen;
import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.integration.mekanism.MekanismIntegration;
import com.direwolf20.laserio.setup.Registration;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(LaserIO.MODID, "jei_plugin");
    }

    private void addHiddenRecipe(List<CraftingRecipe> hiddenRecipes, RecipeManager recipeManager, RegistryObject<Item> itemRegistry) {
        hiddenRecipes.add((CraftingRecipe) recipeManager.byKey(itemRegistry.getId().withSuffix("_nbtclear")).get());
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IRecipeManager recipeRegistry = jeiRuntime.getRecipeManager();
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<CraftingRecipe> hiddenRecipes = new ArrayList<>();

        //Card NBT clearing recipes
        addHiddenRecipe(hiddenRecipes, recipeManager, Registration.CARD_ITEM);
        addHiddenRecipe(hiddenRecipes, recipeManager, Registration.CARD_FLUID);
        addHiddenRecipe(hiddenRecipes, recipeManager, Registration.CARD_ENERGY);
        addHiddenRecipe(hiddenRecipes, recipeManager, Registration.CARD_REDSTONE);

        //Mekanism card NBT clearing recipe (registered only if Mekanism is loaded)
        if (MekanismIntegration.isLoaded()) {
            addHiddenRecipe(hiddenRecipes, recipeManager, Registration.CARD_CHEMICAL);
        }

        //Filter NBT clearing recipes
        addHiddenRecipe(hiddenRecipes, recipeManager, Registration.FILTER_BASIC);
        addHiddenRecipe(hiddenRecipes, recipeManager, Registration.FILTER_COUNT);
        addHiddenRecipe(hiddenRecipes, recipeManager, Registration.FILTER_TAG);
        addHiddenRecipe(hiddenRecipes, recipeManager, Registration.FILTER_NBT);
        addHiddenRecipe(hiddenRecipes, recipeManager, Registration.FILTER_MOD);

        recipeRegistry.hideRecipes(RecipeTypes.CRAFTING, hiddenRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        //Prevent bookmarked items from overlapping with the Card Holder GUI
        registration.addGuiContainerHandler(LaserNodeScreen.class, new GuiContainerHandler<>());
        registration.addGuiContainerHandler(CardItemScreen.class, new GuiContainerHandler<>());
        registration.addGuiContainerHandler(CardEnergyScreen.class, new GuiContainerHandler<>());

        //Add ghost ingredients dragging support for Filters
        registration.addGhostIngredientHandler(CardItemScreen.class, new GhostIngredientHandler<>());
        registration.addGhostIngredientHandler(FilterBasicScreen.class, new GhostIngredientHandler<>());
        registration.addGhostIngredientHandler(FilterCountScreen.class, new GhostIngredientHandler<>());
        registration.addGhostIngredientHandler(FilterNBTScreen.class, new GhostIngredientHandler<>());
        registration.addGhostIngredientHandler(FilterTagScreen.class, new GhostIngredientHandler<>());
    }
}