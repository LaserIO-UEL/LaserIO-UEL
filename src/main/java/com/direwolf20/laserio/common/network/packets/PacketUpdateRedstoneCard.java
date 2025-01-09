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
    byte invert;
    boolean threshold;
    byte thresholdLimit;
    byte thresholdOutput;
    byte combined;
    byte channeltwo;
    //byte special;

    public PacketUpdateRedstoneCard(byte mode, byte channel, boolean strong, byte invert, boolean threshold, byte thresholdLimit, byte thresholdOutput, byte combined, byte channeltwo) {
        this.mode = mode;
        this.channel = channel;
        this.strong = strong;
        this.invert = invert;
        this.threshold = threshold;
        this.thresholdLimit = thresholdLimit;
        this.thresholdOutput = thresholdOutput;
        this.combined = combined;
        this.channeltwo = channeltwo;
        //this.special = special;
    }

    public static void encode(PacketUpdateRedstoneCard msg, FriendlyByteBuf buffer) {
        buffer.writeByte(msg.mode);
        buffer.writeByte(msg.channel);
        buffer.writeBoolean(msg.strong);
        buffer.writeByte(msg.invert);
        buffer.writeBoolean(msg.threshold);
        buffer.writeByte(msg.thresholdLimit);
        buffer.writeByte(msg.thresholdOutput);
        buffer.writeByte(msg.combined);
        buffer.writeByte(msg.channeltwo);
        //buffer.writeByte(msg.special);
    }

    public static PacketUpdateRedstoneCard decode(FriendlyByteBuf buffer) {
        return new PacketUpdateRedstoneCard(buffer.readByte(), buffer.readByte(), buffer.readBoolean(), buffer.readByte(), buffer.readBoolean(), buffer.readByte(), buffer.readByte(), buffer.readByte(), buffer.readByte());
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
                CardRedstone.setCombined(stack, msg.combined);
                CardRedstone.setRedstoneChannelTwo(stack, msg.channeltwo);
                //CardRedstone.setSpecialSetting(stack, msg.special);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}