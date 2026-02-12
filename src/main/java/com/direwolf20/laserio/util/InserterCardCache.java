package com.direwolf20.laserio.util;

import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class InserterCardCache extends BaseCardCache {
    public final DimBlockPos relativePos;
    public final short priority;

    public InserterCardCache(DimBlockPos relativePos, Direction direction, ItemStack cardItem, LaserNodeBE be, int cardSlot) {
        super(direction, cardItem, cardSlot, be);
        this.relativePos = relativePos;
        this.priority = BaseCard.getPriority(cardItem);
    }

    public short getPriority() {
        return priority;
    }

    public double getDistance() {
        return relativePos.blockPos.distSqr(BlockPos.ZERO);
    }

    public boolean isValidDestination(ExtractorCardCache extractorCardCache, Predicate<InserterCardCache> isCardValidForStack) {
        return (channel == extractorCardCache.channel
                && cardType == extractorCardCache.cardType
                && enabled
                && isCardValidForStack.test(this)
                && (!relativePos.blockPos.equals(BlockPos.ZERO)
                    || direction != extractorCardCache.direction
                    || sneaky != extractorCardCache.sneaky)
        );
    }
}