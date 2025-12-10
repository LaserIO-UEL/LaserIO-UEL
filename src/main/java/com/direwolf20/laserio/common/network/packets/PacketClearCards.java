package com.direwolf20.laserio.common.network.packets;

import com.direwolf20.laserio.common.containers.CardHolderContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketClearCards {
    public PacketClearCards() {
    }

    public static void encode(PacketClearCards msg, FriendlyByteBuf buffer) {
    }

    public static PacketClearCards decode(FriendlyByteBuf buffer) {
        return new PacketClearCards();
    }

    public static class Handler {
        public static void handle(PacketClearCards msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer sender = ctx.get().getSender();
                if (sender == null) {
                    return;
                }
                AbstractContainerMenu container = sender.containerMenu;
                if (container == null) {
                    return;
                }
                if (container instanceof CardHolderContainer cardContainer) {
                    cardContainer.clearCards(sender);
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }
}