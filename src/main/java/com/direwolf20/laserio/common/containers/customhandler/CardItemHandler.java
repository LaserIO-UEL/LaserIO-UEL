package com.direwolf20.laserio.common.containers.customhandler;

import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.cards.CardEnergy;
import com.direwolf20.laserio.common.items.filters.BaseFilter;
import com.direwolf20.laserio.common.items.upgrades.OverclockerCard;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CardItemHandler extends ItemStackHandler {
    public ItemStack stack;

    public CardItemHandler(int size, ItemStack itemStack) {
        super(size);
        this.stack = itemStack;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (!stack.isEmpty()) {
            BaseCard.setInventory(stack, this);
        }
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (this.stack.getItem() instanceof CardEnergy) {
            //If in energy card, accept only Energy Overclockers
            return (stack.getItem() instanceof OverclockerCard card && card.getEnergyTier() > 0);
        }
        if (slot == 0) {
            //Allow filter for cards different from Energy
            return stack.getItem() instanceof BaseFilter;
        }
        //Else allow Logistic Overclocker
        return (stack.getItem() instanceof OverclockerCard card && card.getEnergyTier() < 0);
    }

    @Override
    public int getSlotLimit(int slot) {
        //Filters and Energy Overclockers stack to 1
        if (slot == 0) {
            return 1;
        }
        //Logistic Overclockers stack to 4
        return 4;
    }

    public void reSize(int size) {
        NonNullList<ItemStack> newStacks = NonNullList.withSize(size, ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++)
            newStacks.set(i, stacks.get(i));
        stacks = newStacks;
    }
}