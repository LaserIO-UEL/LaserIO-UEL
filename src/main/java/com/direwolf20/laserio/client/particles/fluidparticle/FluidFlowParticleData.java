package com.direwolf20.laserio.client.particles.fluidparticle;

import com.direwolf20.laserio.client.particles.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

import java.util.Locale;

public class FluidFlowParticleData implements ParticleOptions {
    private final FluidStack fluidStack;
    protected final double targetX;
    protected final double targetY;
    protected final double targetZ;
    protected final int ticksPerBlock;

    public FluidFlowParticleData(FluidStack fluidStack, double tx, double ty, double tz, int ticks) {
        this.fluidStack = fluidStack.copy(); //Forge: Fix stack updating after the fact causing particle changes
        targetX = tx;
        targetY = ty;
        targetZ = tz;
        ticksPerBlock = ticks;
    }

    @Nonnull
    @Override
    public ParticleType<FluidFlowParticleData> getType() {
        return ModParticles.FLUID_FLOW_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFluidStack(this.fluidStack);
        buffer.writeDouble(this.targetX);
        buffer.writeDouble(this.targetY);
        buffer.writeDouble(this.targetZ);
        buffer.writeInt(this.ticksPerBlock);
    }

    @Nonnull
    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d",
                this.getType(), this.targetX, this.targetY, this.targetZ, this.ticksPerBlock);
    }

    @OnlyIn(Dist.CLIENT)
    public FluidStack getFluidStack() {
        return this.fluidStack;
    }

    public static final Deserializer<FluidFlowParticleData> DESERIALIZER = new Deserializer<FluidFlowParticleData>() {
        @Nonnull
        @Override
        public FluidFlowParticleData fromCommand(ParticleType<FluidFlowParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double tx = reader.readDouble();
            reader.expect(' ');
            double ty = reader.readDouble();
            reader.expect(' ');
            double tz = reader.readDouble();
            reader.expect(' ');
            int ticks = reader.readInt();
            return new FluidFlowParticleData(FluidStack.EMPTY, tx, ty, tz, ticks);
        }

        @Override
        public FluidFlowParticleData fromNetwork(ParticleType<FluidFlowParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            return new FluidFlowParticleData(buffer.readFluidStack(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt());
        }
    };
}