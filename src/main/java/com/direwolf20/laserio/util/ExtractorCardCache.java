package com.direwolf20.laserio.util;

import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.cards.CardEnergy;
import com.direwolf20.laserio.common.items.cards.CardFluid;
import com.direwolf20.laserio.common.items.cards.CardItem;
import com.direwolf20.laserio.integration.mekanism.CardChemical;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class ExtractorCardCache extends BaseCardCache {
    public final int extractAmt;
    public final int tickSpeed;
    public int remainingSleep;
    public boolean exact;
    public int roundRobin;
    public int energyReceivedExternally;

    public ExtractorCardCache(Direction direction, ItemStack cardItem, int cardSlot, LaserNodeBE be) {
        super(direction, cardItem, cardSlot, be);
        switch(cardType) {
            case ITEM -> {
                this.extractAmt = CardItem.getItemExtractAmt(cardItem);
                this.tickSpeed = CardItem.getExtractSpeed(cardItem);
            }
            case FLUID -> {
                this.extractAmt = CardFluid.getFluidExtractAmt(cardItem);
                this.tickSpeed = CardFluid.getExtractSpeed(cardItem);
            }
            case ENERGY -> {
                this.extractAmt = CardEnergy.getEnergyExtractAmt(cardItem);
                this.tickSpeed = CardEnergy.getExtractSpeed(cardItem);
            }
            case CHEMICAL -> {
                this.extractAmt = CardChemical.getChemicalExtractAmt(cardItem);
                this.tickSpeed = CardChemical.getExtractSpeed(cardItem);
            }
            default -> {
                this.extractAmt = 0;
                this.tickSpeed = 1200;
            }
        }
        this.exact = BaseCard.getExact(cardItem);
        this.roundRobin = BaseCard.getRoundRobin(cardItem);
        this.energyReceivedExternally = 0;
    }

    public int decrementSleep() {
        remainingSleep--;
        if (remainingSleep < 0) {
            remainingSleep = 0;
        }
        return remainingSleep;
    }
}