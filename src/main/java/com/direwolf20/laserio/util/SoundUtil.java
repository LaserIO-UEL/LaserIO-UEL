package com.direwolf20.laserio.util;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class SoundUtil {
    public static void playSound(ServerPlayer player, SoundEvent soundEvent) {
        // Get player's position
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        // Create the packet
        ClientboundSoundPacket packet = new ClientboundSoundPacket(
                Holder.direct(soundEvent), // The sound event
                SoundSource.PLAYERS, // The sound category
                x, y, z, // The sound location
                1, // The volume, 1 is normal, higher is louder
                1, // The pitch, 1 is normal, higher is higher pitch
                1 // A random for some reason? (Some sounds have different variants, like the enchanting table success)
        );

        // Send the packet to the player
        player.connection.send(packet);
    }

    public static void playSound(Player player, SoundEvent soundEvent) {
        if (player instanceof ServerPlayer serverPlayer) {
            playSound(serverPlayer, soundEvent);
        }
    }
}