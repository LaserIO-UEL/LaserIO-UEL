package com.direwolf20.laserio.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class FluidStackKey {
    public Fluid fluid;
    public CompoundTag nbt;
    protected int hash;
    protected static final CompoundTag EMPTY_TAG = new CompoundTag();

    public FluidStackKey(FluidStack stack, boolean compareNBT) {
        this.fluid = stack.getFluid();
        CompoundTag tag = stack.getTag();
        this.nbt = (compareNBT && tag != null) ? tag : EMPTY_TAG;
        this.hash = Objects.hash(fluid, nbt);
    }

    public FluidStackKey() {}

    public FluidStackKey set(FluidStack stack, boolean compareNBT) {
        this.fluid = stack.getFluid();
        CompoundTag tag = stack.getTag();
        this.nbt = (compareNBT && tag != null) ? tag : EMPTY_TAG;
        this.hash = Objects.hash(fluid, nbt);
        return this;
    }

    public FluidStack getStack() {
        return new FluidStack(fluid, 1, nbt);
    }

    public FluidStack getStack(int amt) {
        return new FluidStack(fluid, amt, nbt);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FluidStackKey other) {
            return other.fluid == this.fluid && Objects.equals(other.nbt, this.nbt);
        }
        return false;
    }
}