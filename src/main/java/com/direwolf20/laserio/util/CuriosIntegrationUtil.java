package com.direwolf20.laserio.util;

import com.direwolf20.laserio.common.items.CardHolder;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CuriosIntegrationUtil {
    public static List<ItemStack> findCardHoldersCuriosSlots(Player player) {
        List<ItemStack> cardHolders = new ArrayList<>();
        if (!ModList.get().isLoaded("curios")) {
            return cardHolders;
        }
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("card_holder").ifPresent(slotInventory -> {
                IDynamicStackHandler possibleCardHolders = slotInventory.getStacks();
                for (int i = 0; i < possibleCardHolders.getSlots(); i++) {
                    ItemStack possibleCardHolder = possibleCardHolders.getStackInSlot(i);
                    if (possibleCardHolder.getItem() instanceof CardHolder) {
                        cardHolders.add(possibleCardHolder);
                    }
                }
            });
        });
        return cardHolders;
    }

    public static ItemStack findFirstCardHolderCuriosSlots(Player player) {
        List<ItemStack> cardHolders = findCardHoldersCuriosSlots(player);
        return (cardHolders.isEmpty() ? ItemStack.EMPTY : cardHolders.get(0));
    }

    public static ItemStack findSpecificCardHolderCuriosSlots(Player player, UUID cardHolderUUID) {
        for (ItemStack cardHolder : findCardHoldersCuriosSlots(player)) {
            if (CardHolder.getUUID(cardHolder).equals(cardHolderUUID)) {
                return cardHolder;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean isCardHolderInCuriosSlots(Player player, UUID cardHolderUUID) {
        return !(findSpecificCardHolderCuriosSlots(player, cardHolderUUID).isEmpty());
    }
}