package com.direwolf20.laserio.common.network.packets;

import com.direwolf20.laserio.common.blocks.LaserNode;
import com.direwolf20.laserio.common.containers.CardHolderContainer;
import com.direwolf20.laserio.common.items.CardHolder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class PacketKeybindPerformAction {
    private byte keybindAction;

    public PacketKeybindPerformAction(byte keybindAction) {
        this.keybindAction = keybindAction;
    }

    public static void encode(PacketKeybindPerformAction msg, FriendlyByteBuf buffer) {
        buffer.writeByte(msg.keybindAction);
    }

    public static PacketKeybindPerformAction decode(FriendlyByteBuf buffer) {
        return new PacketKeybindPerformAction(buffer.readByte());
    }

    public static class Handler {
        public static void handle(PacketKeybindPerformAction msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer sender = ctx.get().getSender();
                if (sender == null) {
                    return;
                }
                ItemStack cardHolder = LaserNode.findFirstCardHolder(sender);
                if (cardHolder.isEmpty()) {
                    return;
                }
                if (msg.keybindAction == 0) {
                    cardHolder.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(handler -> {
                        NetworkHooks.openScreen(sender, new SimpleMenuProvider(
                                (windowId, playerInventory, playerEntity) -> new CardHolderContainer(windowId, playerInventory, sender, cardHolder, handler), Component.translatable("")), (buf -> {
                            buf.writeItem(cardHolder);
                        }));
                    });
                } else {
                    CardHolder.setActive(cardHolder, !CardHolder.getActive(cardHolder));
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }
}