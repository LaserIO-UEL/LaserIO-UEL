package com.direwolf20.laserio.common.items.cards;

import com.direwolf20.laserio.common.containers.CardRedstoneContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class CardRedstone extends BaseCard {

    public CardRedstone() {
        super();
        CARDTYPE = BaseCard.CardType.REDSTONE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack card = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(card);
        }
        NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new CardRedstoneContainer(windowId, playerInventory, player, card), Component.translatable("")), (buf -> {
            buf.writeItem(card);
            buf.writeByte(-1);
        }));
        return InteractionResultHolder.pass(card);
    }

    public static byte nextTransferMode(ItemStack card) {
        byte mode = getTransferMode(card);
        return setTransferMode(card, (byte) (mode == 1 ? 0 : mode + 1));
    }

    @Deprecated(since = "1.5.0", forRemoval = true)
    @MigrateThresholdToInterval
    private static boolean migrateThresholdToInterval(ItemStack card, CompoundTag compound) {
        if (compound == null || !compound.contains("redstonethreshold")) return false;
        boolean threshold = compound.getBoolean("redstonethreshold");
        card.removeTagKey("redstonethreshold");
        card.getOrCreateTag().putBoolean("redstoneinterval", threshold);
        return threshold;
    }

    public static boolean getInterval(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("redstoneinterval")) return migrateThresholdToInterval(card, compound);
        return compound.getBoolean("redstoneinterval");
    }

    public static boolean setInterval(ItemStack card, boolean interval) {
        if (!interval)
            card.removeTagKey("redstoneinterval");
        else
            card.getOrCreateTag().putBoolean("redstoneinterval", interval);
        return interval;
    }

    @Deprecated(since = "1.5.0", forRemoval = true)
    @MigrateThresholdToInterval
    private static byte migrateThresholdLimitToIntervalLowerBound(ItemStack card, CompoundTag compound) {
        if (compound == null || !compound.contains("redstonethresholdlimit")) return 0;
        byte thresholdLimit = compound.getByte("redstonethresholdlimit");
        card.removeTagKey("redstonethresholdlimit");
        card.getOrCreateTag().putByte("redstoneintervallowerbound", thresholdLimit);
        return thresholdLimit;
    }

    public static byte getIntervalLowerBound(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("redstoneintervallowerbound")) return migrateThresholdLimitToIntervalLowerBound(card, compound);
        return compound.getByte("redstoneintervallowerbound");
    }

    public static byte setIntervalLowerBound(ItemStack card, byte intervalLowerBound) {
        if (intervalLowerBound == 0)
            card.removeTagKey("redstoneintervallowerbound");
        else
            card.getOrCreateTag().putByte("redstoneintervallowerbound", intervalLowerBound);
        return intervalLowerBound;
    }

    public static byte getIntervalUpperBound(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("redstoneintervalupperbound")) return 15;
        return compound.getByte("redstoneintervalupperbound");
    }

    public static byte setIntervalUpperBound(ItemStack card, byte intervalUpperBound) {
        if (intervalUpperBound == 15)
            card.removeTagKey("redstoneintervalupperbound");
        else
            card.getOrCreateTag().putByte("redstoneintervalupperbound", intervalUpperBound);
        return intervalUpperBound;
    }

    @Deprecated(since = "1.5.0", forRemoval = true)
    @MigrateThresholdToInterval
    private static byte migrateThresholdOutputToIntervalOutput(ItemStack card, CompoundTag compound) {
        if (compound == null || !compound.contains("redstonethresholdoutput")) return 15;
        byte thresholdOutput = compound.getByte("redstonethresholdoutput");
        card.removeTagKey("redstonethresholdoutput");
        card.getOrCreateTag().putByte("redstoneintervaloutput", thresholdOutput);
        return thresholdOutput;
    }

    public static byte getIntervalOutput(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("redstoneintervaloutput")) return migrateThresholdOutputToIntervalOutput(card, compound);
        return compound.getByte("redstoneintervaloutput");
    }

    public static byte setIntervalOutput(ItemStack card, byte intervalOutput) {
        if (intervalOutput == 15)
            card.removeTagKey("redstoneintervaloutput");
        else
            card.getOrCreateTag().putByte("redstoneintervaloutput", intervalOutput);
        return intervalOutput;
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
        if (compound == null || !compound.contains("redstoneoutputmode")) return 0;
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
        if (compound == null || !compound.contains("redstonelogicoperation")) return 0;
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
        if (compound == null || !compound.contains("redstonechanneloperation")) return 0;
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

    @Target(ElementType.METHOD)
    private @interface MigrateThresholdToInterval {}
}