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
    boolean threshold;
    byte thresholdLimit;
    byte thresholdOutput;
    boolean strong;
    byte outputMode;
    byte logicOperation;
    byte logicOperationChannel;
    //byte specialFeature;

    public PacketUpdateRedstoneCard(byte mode, byte channel, boolean threshold, byte thresholdLimit, byte thresholdOutput, boolean strong, byte outputMode, byte logicOperation, byte logicOperationChannel) {
        this.mode = mode;
        this.channel = channel;
        this.strong = strong;
        this.threshold = threshold;
        this.thresholdLimit = thresholdLimit;
        this.thresholdOutput = thresholdOutput;
        this.outputMode = outputMode;
        this.logicOperation = logicOperation;
        this.logicOperationChannel = logicOperationChannel;
        //this.specialFeature = specialFeature;
    }

    public static void encode(PacketUpdateRedstoneCard msg, FriendlyByteBuf buffer) {
        buffer.writeByte(msg.mode);
        buffer.writeByte(msg.channel);
        buffer.writeBoolean(msg.threshold);
        buffer.writeByte(msg.thresholdLimit);
        buffer.writeByte(msg.thresholdOutput);
        buffer.writeBoolean(msg.strong);
        buffer.writeByte(msg.outputMode);
        buffer.writeByte(msg.logicOperation);
        buffer.writeByte(msg.logicOperationChannel);
        //buffer.writeByte(msg.specialFeature);
    }

    public static PacketUpdateRedstoneCard decode(FriendlyByteBuf buffer) {
        return new PacketUpdateRedstoneCard(buffer.readByte(), buffer.readByte(), buffer.readBoolean(), buffer.readByte(), buffer.readByte(), buffer.readBoolean(), buffer.readByte(), buffer.readByte(), buffer.readByte());
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
                CardRedstone.setThreshold(stack, msg.threshold);
                CardRedstone.setThresholdLimit(stack, msg.thresholdLimit);
                CardRedstone.setThresholdOutput(stack, msg.thresholdOutput);
                CardRedstone.setStrong(stack, msg.strong);
                CardRedstone.setOutputMode(stack, msg.outputMode);
                CardRedstone.setLogicOperation(stack, msg.logicOperation);
                CardRedstone.setRedstoneChannelOperation(stack, msg.logicOperationChannel);
                //CardRedstone.setSpecialFeature(stack, msg.specialFeature);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}