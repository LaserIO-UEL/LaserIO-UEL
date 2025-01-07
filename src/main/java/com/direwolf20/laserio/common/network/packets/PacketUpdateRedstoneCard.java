package com.direwolf20.laserio.common.network.packets;

import com.direwolf20.laserio.common.containers.CardRedstoneContainer;
import com.direwolf20.laserio.common.items.cards.CardRedstone;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateRedstoneCard {
    byte mode;
    byte channel;
    boolean strong;
    boolean invert;
    boolean threshold;
    byte thresholdLimit;
    byte thresholdOutput;
    boolean blockRedstone;

    public PacketUpdateRedstoneCard(byte mode, byte channel, boolean strong, boolean invert, boolean threshold, byte thresholdLimit, byte thresholdOutput, boolean blockRedstone) {
        this.mode = mode;
        this.channel = channel;
        this.strong = strong;
        this.invert = invert;
        this.threshold = threshold;
        this.thresholdLimit = thresholdLimit;
        this.thresholdOutput = thresholdOutput;
        this.blockRedstone = blockRedstone;
    }

    public static void encode(PacketUpdateRedstoneCard msg, FriendlyByteBuf buffer) {
        buffer.writeByte(msg.mode);
        buffer.writeByte(msg.channel);
        buffer.writeBoolean(msg.strong);
        buffer.writeBoolean(msg.invert);
        buffer.writeBoolean(msg.threshold);
        buffer.writeByte(msg.thresholdLimit);
        buffer.writeByte(msg.thresholdOutput);
        buffer.writeBoolean(msg.blockRedstone);
    }

    public static PacketUpdateRedstoneCard decode(FriendlyByteBuf buffer) {
        return new PacketUpdateRedstoneCard(buffer.readByte(), buffer.readByte(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readByte(), buffer.readByte(), buffer.readBoolean());
    }

    public static class Handler {
        public static void handle(PacketUpdateRedstoneCard msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;

                AbstractContainerMenu container = player.containerMenu;
                if (container == null)
                    return;

                if (!(container instanceof CardRedstoneContainer))
                    return;

                ItemStack stack;
                stack = ((CardRedstoneContainer) container).cardItem;
                CardRedstone.setTransferMode(stack, msg.mode);
                CardRedstone.setRedstoneChannel(stack, msg.channel);
                CardRedstone.setStrong(stack, msg.strong);
                CardRedstone.setInvert(stack, msg.invert);
                CardRedstone.setThreshold(stack, msg.threshold);
                CardRedstone.setThresholdLimit(stack, msg.thresholdLimit);
                CardRedstone.setThresholdOutput(stack, msg.thresholdOutput);
                CardRedstone.setBlockRedstone(stack, msg.blockRedstone);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}