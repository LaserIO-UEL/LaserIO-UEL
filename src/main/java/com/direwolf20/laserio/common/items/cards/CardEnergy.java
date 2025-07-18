package com.direwolf20.laserio.common.items.cards;

import com.direwolf20.laserio.common.containers.CardEnergyContainer;
import com.direwolf20.laserio.common.containers.customhandler.CardItemHandler;
import com.direwolf20.laserio.setup.Config;
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

public class CardEnergy extends BaseCard {
    public CardEnergy() {
        super();
        CARDTYPE = CardType.ENERGY;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack card = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(card);
        }
        NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new CardEnergyContainer(windowId, playerInventory, player, card), Component.translatable("")), (buf -> {
            buf.writeItem(card);
            buf.writeByte(-1);
        }));
        return InteractionResultHolder.pass(card);
    }

    public static CardItemHandler getInventory(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("inv")) return new CardItemHandler(CardEnergyContainer.SLOTS, card);
        CardItemHandler handler = new CardItemHandler(CardEnergyContainer.SLOTS, card);
        handler.deserializeNBT(compound.getCompound("inv"));
        if (handler.getSlots() < CardEnergyContainer.SLOTS)
            handler.reSize(CardEnergyContainer.SLOTS);
        return handler;
    }

    public static CardItemHandler setInventory(ItemStack card, CardItemHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                card.getOrCreateTag().put("inv", handler.serializeNBT());
                return handler;
            }
        }
        card.removeTagKey("inv");
        return handler;
    }

    public static int setEnergyExtractAmt(ItemStack card, int energyextractamt) {
        if (energyextractamt == Config.MAX_FE_NO_TIERS.get())
            card.removeTagKey("energyextractamt");
        else
            card.getOrCreateTag().putInt("energyextractamt", energyextractamt);
        return energyextractamt;
    }

    public static int getEnergyExtractAmt(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("energyextractamt")) return Config.MAX_FE_NO_TIERS.get();
        return compound.getInt("energyextractamt");
    }

    public static int setExtractSpeed(ItemStack card, int itemextractspeed) {
        if (itemextractspeed == Config.MIN_TICKS_ENERGY.get())
            card.removeTagKey("itemextractspeed");
        else
            card.getOrCreateTag().putInt("itemextractspeed", itemextractspeed);
        return itemextractspeed;
    }

    public static int getExtractSpeed(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("itemextractspeed")) return Config.MIN_TICKS_ENERGY.get();
        return compound.getInt("itemextractspeed");
    }

    public static int setInsertLimitPercent(ItemStack card, int limitpercent) {
        if (limitpercent == 100)
            card.removeTagKey("limitinsertpercent");
        else
            card.getOrCreateTag().putInt("limitinsertpercent", limitpercent);
        return limitpercent;
    }

    public static int getInsertLimitPercent(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("limitinsertpercent")) return 100;
        return compound.getInt("limitinsertpercent");
    }

    public static int setExtractLimitPercent(ItemStack card, int limitextractpercent) {
        if (limitextractpercent == 0)
            card.removeTagKey("limitextractpercent");
        else
            card.getOrCreateTag().putInt("limitextractpercent", limitextractpercent);
        return limitextractpercent;
    }

    public static int getExtractLimitPercent(ItemStack card) {
        CompoundTag compound = card.getTag();
        if (compound == null || !compound.contains("limitextractpercent")) return 0;
        return compound.getInt("limitextractpercent");
    }
}