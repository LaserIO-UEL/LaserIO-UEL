package com.direwolf20.laserio.client.events;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.blocks.LaserNode;
import com.direwolf20.laserio.common.items.CardHolder;
import com.direwolf20.laserio.common.network.PacketHandler;
import com.direwolf20.laserio.common.network.packets.PacketKeybindPerformAction;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    public static final KeyMapping OPEN_CARD_HOLDER = new KeyMapping(
            "key.laserio.open_card_holder",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            LaserIO.MODNAME
    );
    public static final KeyMapping TOGGLE_CARD_HOLDER_PULLING = new KeyMapping(
            "key.laserio.toggle_card_holder_pulling",
            KeyConflictContext.IN_GAME,
            KeyModifier.SHIFT,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            LaserIO.MODNAME
    );

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != InputConstants.PRESS) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (OPEN_CARD_HOLDER.isDown()) {
            if (!LaserNode.findFirstCardHolder(player).isEmpty()) {
                PacketHandler.sendToServer(new PacketKeybindPerformAction((byte) 0));
            }
        } else if (TOGGLE_CARD_HOLDER_PULLING.isDown()) {
            ItemStack cardHolder = LaserNode.findFirstCardHolder(player);
            if (!cardHolder.isEmpty()) {
                String translationKey = "message.laserio.card_holder_pulling_" + (CardHolder.getActive(cardHolder) ? "disabled" : "enabled");
                player.displayClientMessage(Component.translatable(translationKey), true);
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
                PacketHandler.sendToServer(new PacketKeybindPerformAction((byte) 1));
            }
        }
    }
}