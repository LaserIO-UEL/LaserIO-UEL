package com.direwolf20.laserio.integration.curios;

import com.direwolf20.laserio.common.items.CardHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class CuriosIntegration {
    private CuriosIntegration() {}

    public static ItemStack findFirstCardHolder(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(inv -> inv.findFirstCurio(stack -> stack.getItem() instanceof CardHolder)
                        .map(SlotResult::stack)
                        .orElse(ItemStack.EMPTY))
                .orElse(ItemStack.EMPTY);
    }
}