package com.direwolf20.laserio.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ItemStackKey {
    public Item item;
    public CompoundTag nbt;
    protected int hash;
    protected static final CompoundTag EMPTY_TAG = new CompoundTag();

    public ItemStackKey(ItemStack stack, boolean compareNBT) {
        this.item = stack.getItem();
        CompoundTag tag = stack.getTag();
        this.nbt = (compareNBT && tag != null) ? tag : EMPTY_TAG;
        this.hash = Objects.hash(item, nbt);
    }

    public ItemStackKey() {}

    public ItemStackKey set(ItemStack stack, boolean compareNBT) {
        this.item = stack.getItem();
        CompoundTag tag = stack.getTag();
        this.nbt = (compareNBT && tag != null) ? tag : EMPTY_TAG;
        this.hash = Objects.hash(item, nbt);
        return this;
    }

    public ItemStack getStack() {
        return new ItemStack(item, 1, nbt);
    }

    public ItemStack getStack(int amt) {
        return new ItemStack(item, amt, nbt);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStackKey other) {
            return other.item == this.item && Objects.equals(other.nbt, this.nbt);
        }
        return false;
    }
}