package com.direwolf20.laserio.common.items.cards;

import com.direwolf20.laserio.common.containers.CardRedstoneContainer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class CardRedstone extends BaseCard {

    public CardRedstone() {
        super();
        CARDTYPE = BaseCard.CardType.REDSTONE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (level.isClientSide()) return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);

        NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new CardRedstoneContainer(windowId, playerInventory, player, itemstack), Component.translatable("")), (buf -> {
            buf.writeItem(itemstack);
            buf.writeByte(-1);
        }));

        return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
    }

    public static byte nextTransferMode(ItemStack card) {
        byte mode = getTransferMode(card);
        return setTransferMode(card, (byte) (mode == 1 ? 0 : mode + 1));
    }

    public static boolean getStrong(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if (compound == null || !compound.contains("redstonestrong")) return false;
        return compound.getBoolean("redstonestrong");
    }

    public static boolean setStrong(ItemStack stack, boolean strong) {
        if (!strong)
            stack.removeTagKey("redstonestrong");
        else
            stack.getOrCreateTag().putBoolean("redstonestrong", strong);
        return strong;
    }

    public static boolean getInvert(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if (compound == null || !compound.contains("redstoneinvert")) return false;
        return compound.getBoolean("redstoneinvert");
    }

    public static boolean setInvert(ItemStack stack, boolean invert) {
        if (!invert)
            stack.removeTagKey("redstoneinvert");
        else
            stack.getOrCreateTag().putBoolean("redstoneinvert", invert);
        return invert;
    }

    public static boolean getThreshold(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if (compound == null || !compound.contains("redstonethreshold")) return false;
        return compound.getBoolean("redstonethreshold");
    }

    public static boolean setThreshold(ItemStack stack, boolean threshold) {
        if (!threshold)
            stack.removeTagKey("redstonethreshold");
        else
            stack.getOrCreateTag().putBoolean("redstonethreshold", threshold);
        return threshold;
    }

    public static byte setThresholdLimit(ItemStack card, byte limit) {
        if (limit == 0)
            card.removeTagKey("thresholdlimit");
        else
            card.getOrCreateTag().putByte("thresholdlimit", limit);
        return limit;
    }

    public static byte getThresholdLimit(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("thresholdlimit")) return (byte) 0;
        return compound.getByte("thresholdlimit");
    }

    public static byte setThresholdOutput(ItemStack card, byte output) {
        if (output == 15)
            card.removeTagKey("thresholdoutput");
        else
            card.getOrCreateTag().putByte("thresholdoutput", output);
        return output;
    }

    public static byte getThresholdOutput(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("thresholdoutput")) return (byte) 15;
        return compound.getByte("thresholdoutput");
    }
}