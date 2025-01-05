package com.direwolf20.laserio.util;

import com.direwolf20.laserio.client.screens.CardItemScreen;
import com.direwolf20.laserio.client.screens.FilterCountScreen;
import com.direwolf20.laserio.common.containers.customslot.FilterBasicSlot;
import com.direwolf20.laserio.common.network.PacketHandler;
import com.direwolf20.laserio.common.network.packets.PacketGhostSlot;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler.Target;
import mezz.jei.api.ingredients.ITypedIngredient;

import mekanism.api.IMekanismAccess;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class JEIIntegrationUtil {
    public static <I> List<Target<I>> getTargetsTyped(AbstractContainerScreen<? extends AbstractContainerMenu> gui, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();

        for (Slot slot : gui.getMenu().slots) {
            if (!slot.isActive()) {
                continue;
            }

            Rect2i bounds = new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16); //RS Had this as 17 17

            if (ingredient.getIngredient() instanceof ItemStack && (slot instanceof FilterBasicSlot)) {
                targets.add(new Target<I>() {
                    @Override
                    public Rect2i getArea() {
                        return bounds;
                    }

                    @Override
                    public void accept(I ingredient) {
                        ItemStack itemStack = (ItemStack) ingredient;
                        slot.set((gui instanceof CardItemScreen) ? itemStack.copy() : itemStack);
                        if (gui instanceof FilterCountScreen filterCountGui)
                            filterCountGui.getMenu().handler.setStackInSlotSave(slot.index, itemStack); //We do this for continuity between client/server -- not needed in cardItemScreen
                        PacketHandler.sendToServer(new PacketGhostSlot(slot.index, itemStack, itemStack.getCount()));
                   }
                });
            } else if (ingredient.getIngredient() instanceof FluidStack && (slot instanceof FilterBasicSlot)) {
                targets.add(new Target<I>() {
                    @Override
                    public Rect2i getArea() {
                        return bounds;
                    }

                    @Override
                    public void accept(I ingredient) {
                        ItemStack itemStack = new ItemStack(((FluidStack) ingredient).getFluid().getBucket(), 1);
                        slot.set(itemStack);
                        if (gui instanceof FilterCountScreen filterCountGui)
                            filterCountGui.getMenu().handler.setStackInSlotSave(slot.index, itemStack); //We do this for continuity between client/server -- not needed in cardItemScreen
                        PacketHandler.sendToServer(new PacketGhostSlot(slot.index, itemStack, itemStack.getCount()));
                    }
                });
            } else if (ingredient.getIngredient() instanceof ChemicalStack<?> && (slot instanceof FilterBasicSlot)) {
                targets.add(new Target<I>() {
                    @Override
                    public Rect2i getArea() {
                        return bounds;
                    }

                    @Override
                    public void accept(I ingredient) {
                        ItemStack itemStack = ItemStack.EMPTY;
                        if (ingredient instanceof GasStack gasStack) {
                            itemStack = IMekanismAccess.INSTANCE.jeiHelper().getGasStackHelper().getCheatItemStack(gasStack);
                        } else if (ingredient instanceof SlurryStack slurryStack) {
                            itemStack = IMekanismAccess.INSTANCE.jeiHelper().getSlurryStackHelper().getCheatItemStack(slurryStack);
                        } else if (ingredient instanceof PigmentStack pigmentStack) {
                            itemStack = IMekanismAccess.INSTANCE.jeiHelper().getPigmentStackHelper().getCheatItemStack(pigmentStack);
                        } else if (ingredient instanceof InfusionStack infusionStack) {
                            itemStack = IMekanismAccess.INSTANCE.jeiHelper().getInfusionStackHelper().getCheatItemStack(infusionStack);
                        }
                        slot.set(itemStack);
                        if (gui instanceof FilterCountScreen filterCountGui)
                            filterCountGui.getMenu().handler.setStackInSlotSave(slot.index, itemStack); //We do this for continuity between client/server -- not needed in cardItemScreen
                        PacketHandler.sendToServer(new PacketGhostSlot(slot.index, itemStack, itemStack.getCount()));
                    }
                });
            }
        }
        return targets;
    }
}