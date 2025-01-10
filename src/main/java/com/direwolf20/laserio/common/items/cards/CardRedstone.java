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

    public static byte getThresholdLimit(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("redstonethresholdlimit")) return (byte) 0;
        return compound.getByte("redstonethresholdlimit");
    }

    public static byte setThresholdLimit(ItemStack card, byte thresholdLimit) {
        if (thresholdLimit == 0)
            card.removeTagKey("redstonethresholdlimit");
        else
            card.getOrCreateTag().putByte("redstonethresholdlimit", thresholdLimit);
        return thresholdLimit;
    }

    public static byte getThresholdOutput(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("redstonethresholdoutput")) return (byte) 15;
        return compound.getByte("redstonethresholdoutput");
    }

    public static byte setThresholdOutput(ItemStack card, byte thresholdOutput) {
        if (thresholdOutput == 15)
            card.removeTagKey("redstonethresholdoutput");
        else
            card.getOrCreateTag().putByte("redstonethresholdoutput", thresholdOutput);
        return thresholdOutput;
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

    public static byte getOutputMode(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if (compound == null || !compound.contains("redstoneoutputmode")) return (byte) 0;
        return compound.getByte("redstoneoutputmode");
    }

    public static byte setOutputMode(ItemStack stack, byte outputMode) {
        if (outputMode == 0)
            stack.removeTagKey("redstoneoutputmode");
        else
            stack.getOrCreateTag().putByte("redstoneoutputmode", outputMode);
        return outputMode;
    }

    public static byte getLogicOperation(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("redstonelogicoperation")) return (byte) 0;
        return compound.getByte("redstonelogicoperation");
    }

    public static byte setLogicOperation(ItemStack card, byte logicOperation) {
        if (logicOperation == 0)
            card.removeTagKey("redstonelogicoperation");
        else
            card.getOrCreateTag().putByte("redstonelogicoperation", logicOperation);
        return logicOperation;
    }

    public static byte getRedstoneChannelOperation(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("redstonechanneloperation")) return (byte) 0;
        return compound.getByte("redstonechanneloperation");
    }

    public static byte setRedstoneChannelOperation(ItemStack card, byte logicOperationChannel) {
        if (logicOperationChannel == 0)
            card.removeTagKey("redstonechanneloperation");
        else
            card.getOrCreateTag().putByte("redstonechanneloperation", logicOperationChannel);
        return logicOperationChannel;
    }

    public static byte nextRedstoneChannelOperation(ItemStack card) {
        byte k = getRedstoneChannelOperation(card);
        return setRedstoneChannelOperation(card, (byte) (k == 15 ? 0 : k + 1));
    }

    public static byte previousRedstoneChannelOperation(ItemStack card) {
        byte k = getRedstoneChannelOperation(card);
        return setRedstoneChannelOperation(card, (byte) (k == 0 ? 15 : k - 1));
    }
    /*
    public static byte getSpecialFeature(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("specialfeature")) return (byte) 0;
        return compound.getByte("specialfeature");
    }

    public static byte setSpecialFeature(ItemStack card, byte specialFeature) {
        if (specialFeature == 0)
            card.removeTagKey("specialfeature");
        else
            card.getOrCreateTag().putByte("specialfeature", specialFeature);
        return specialFeature;
    }
    */
}