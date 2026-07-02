package com.direwolf20.laserio.util;

import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.cards.BaseCard.CardType;
import com.direwolf20.laserio.common.items.cards.BaseCard.TransferMode;
import com.direwolf20.laserio.common.items.cards.CardEnergy;
import com.direwolf20.laserio.common.items.cards.CardFluid;
import com.direwolf20.laserio.common.items.cards.CardItem;
import com.direwolf20.laserio.common.items.cards.CardRedstone;
import com.direwolf20.laserio.common.items.filters.BaseFilter;
import com.direwolf20.laserio.common.items.filters.FilterBasic;
import com.direwolf20.laserio.common.items.filters.FilterCount;
import com.direwolf20.laserio.common.items.filters.FilterMod;
import com.direwolf20.laserio.common.items.filters.FilterNBT;
import com.direwolf20.laserio.common.items.filters.FilterTag;
import com.direwolf20.laserio.integration.mekanism.common.items.cards.CardChemical;
import com.direwolf20.laserio.integration.mekanism.util.MekanismCardCache;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BaseCardCache {
    public final Direction direction;
    public final ItemStack cardItem;
    public final byte channel;
    public final byte redstoneMode;
    public final byte redstoneChannel;
    public final ItemStack filterCard;
    public final int cardSlot;
    public final List<ItemStack> filteredItems;
    public final List<ItemStackKey> filteredItemsKeys;
    public final List<FluidStack> filteredFluids;
    public final List<FluidStackKey> filteredFluidsKeys;
    public final Set<String> filterTags;
    public final Set<String> filterNBTs;
    public final byte sneaky;
    public final LaserNodeBE be;
    public final CardType cardType;
    public int extractLimit = 0;
    public int insertLimit = 0;
    public boolean enabled = true;

    public final boolean isAllowList;
    public final boolean isCompareNBT;
    public final Map<ItemStackKey, Boolean> filterCache = new Object2BooleanOpenHashMap<>();
    public final Map<ItemStackKey, Integer> filterCounts = new Object2IntOpenHashMap<>();
    private final ItemStackKey lookupKey = new ItemStackKey();
    //Fluids
    public final Map<FluidStackKey, Boolean> filterCacheFluid = new Object2BooleanOpenHashMap<>();
    public final Map<FluidStackKey, Integer> filterCountsFluid = new Object2IntOpenHashMap<>();
    private final FluidStackKey lookupKeyFluid = new FluidStackKey();
    //Mekanism chemicals
    public MekanismCardCache mekanismCardCache;

    public BaseCardCache(Direction direction, ItemStack cardItem, int cardSlot, LaserNodeBE be) {
        this.cardItem = cardItem;
        this.direction = direction;
        this.sneaky = BaseCard.getSneaky(cardItem);
        this.channel = BaseCard.getChannel(cardItem);
        this.redstoneMode = BaseCard.getRedstoneMode(cardItem);
        this.redstoneChannel = BaseCard.getRedstoneChannel(cardItem);
        this.filterCard = BaseCard.getFilter(cardItem);
        this.cardSlot = cardSlot;
        if (cardItem.getItem() instanceof CardItem) {
            cardType = CardType.ITEM;
        } else if (cardItem.getItem() instanceof CardFluid) {
            cardType = CardType.FLUID;
        } else if (cardItem.getItem() instanceof CardEnergy) {
            cardType = CardType.ENERGY;
            this.insertLimit = CardEnergy.getInsertLimitPercent(cardItem);
            this.extractLimit = CardEnergy.getExtractLimitPercent(cardItem);
        } else if (cardItem.getItem() instanceof CardRedstone) {
            cardType = CardType.REDSTONE;
        } else if (cardItem.getItem() instanceof CardChemical) {
            cardType = CardType.CHEMICAL;
            mekanismCardCache = new MekanismCardCache(this);
        } else {
            cardType = CardType.MISSING;
        }
        this.be = be;
        if (filterCard.isEmpty()) {
            filteredItems = new ArrayList<>();
            filteredItemsKeys = new ArrayList<>();
            filteredFluids = new ArrayList<>();
            filteredFluidsKeys = new ArrayList<>();
            filterTags = new HashSet<>();
            filterNBTs = new HashSet<>();
            isAllowList = false;
            isCompareNBT = false;
        } else {
            isCompareNBT = BaseFilter.getCompareNBT(filterCard);
            this.filteredItems = getFilteredItems();
            this.filteredItemsKeys = new ArrayList<>();
            for (ItemStack stack : filteredItems) {
                filteredItemsKeys.add(new ItemStackKey(stack, isCompareNBT));
            }
            this.filteredFluids = getFilteredFluids();
            this.filteredFluidsKeys = new ArrayList<>();
            for (FluidStack stack : filteredFluids) {
                filteredFluidsKeys.add(new FluidStackKey(stack, isCompareNBT));
            }
            this.filterTags = new HashSet<>(getFilterTags());
            this.filterNBTs = new HashSet<>(getFilterNBTs());
            isAllowList = BaseFilter.getAllowList(filterCard);
        }
        setEnabled();
    }

    public void setEnabled() {
        if (redstoneMode == 0 || BaseCard.getNamedTransferMode(cardItem) == TransferMode.SENSOR) { //Sensors are always enabled
            enabled = true;
        } else {
            byte strength = be.getRedstoneChannelStrength(redstoneChannel);
            if (strength > 0 && redstoneMode == 1) {
                enabled = false;
            } else if (strength == 0 && redstoneMode == 2) {
                enabled = false;
            } else {
                enabled = true;
            }
        }
    }

    public int getFilterAmt(ItemStack testStack) {
        if (filterCard.isEmpty())
            return 0; //If theres no filter in the card (This should never happen in theory)
        if (!(filterCard.getItem() instanceof FilterCount)) { //If this is a basic or tag Card return -1 which will mean infinite amount
            return -1;
        }
        lookupKey.set(testStack, isCompareNBT);
        Integer cachedCount = filterCounts.get(lookupKey);
        if (cachedCount != null) //If we've already tested this, get it from the cache
            return cachedCount;

        for (int i = 0; i < filteredItemsKeys.size(); i++) {
            if (filteredItemsKeys.get(i).equals(lookupKey)) {
                ItemStackKey key = new ItemStackKey(testStack, isCompareNBT);
                int count = filteredItems.get(i).getCount();
                filterCounts.put(key, count);
                return count;
            }
        }
        ItemStackKey key = new ItemStackKey(testStack, isCompareNBT);
        filterCounts.put(key, 0);
        return 0; //Should never get here in theory
    }

    public int getFilterAmt(FluidStack testStack) {
        if (filterCard.isEmpty())
            return 0; //If theres no filter in the card (This should never happen in theory)
        if (!(filterCard.getItem() instanceof FilterCount)) { //If this is a basic or tag Card return -1 which will mean infinite amount
            return -1;
        }
        lookupKeyFluid.set(testStack, isCompareNBT);
        Integer cachedMbAmt = filterCountsFluid.get(lookupKeyFluid);
        if (cachedMbAmt != null) //If we've already tested this, get it from the cache
            return cachedMbAmt;

        ItemStackHandler filterSlotHandler = FilterCount.getInventory(filterCard);
        for (int i = 0; i < filterSlotHandler.getSlots(); i++) { //Gotta iterate the card's NBT because of the way we store amounts (in the MBAmt tag)
            ItemStack itemStack = filterSlotHandler.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                LazyOptional<IFluidHandlerItem> fluidHandlerOptional = FluidUtil.getFluidHandler(itemStack);
                if (!fluidHandlerOptional.isPresent()) continue;
                IFluidHandler fluidHandler = fluidHandlerOptional.resolve().get();
                for (int tank = 0; tank < fluidHandler.getTanks(); tank++) {
                    FluidStack fluidStack = fluidHandler.getFluidInTank(tank);
                    // Using a local lookup key for internal comparison to avoid allocation
                    FluidStackKey innerLookup = new FluidStackKey();
                    if (innerLookup.set(fluidStack, isCompareNBT).equals(lookupKeyFluid)) {
                        int mbAmt = FilterCount.getSlotAmount(filterCard, i);
                        FluidStackKey key = new FluidStackKey(testStack, isCompareNBT);
                        filterCountsFluid.put(key, mbAmt);
                        return mbAmt;
                    }
                }
            }
        }
        FluidStackKey key = new FluidStackKey(testStack, isCompareNBT);
        filterCountsFluid.put(key, 0);
        return 0; //Should never get here in theory
    }

    public List<ItemStack> getFilteredItems() {
        List<ItemStack> filteredItems = new ArrayList<>();
        ItemStackHandler filterSlotHandler;
        if (filterCard.getItem() instanceof FilterBasic)
            filterSlotHandler = FilterBasic.getInventory(filterCard);
        else
            filterSlotHandler = FilterCount.getInventory(filterCard);
        for (int i = 0; i < filterSlotHandler.getSlots(); i++) {
            ItemStack itemStack = filterSlotHandler.getStackInSlot(i);
            if (!itemStack.isEmpty())
                filteredItems.add(itemStack); //If this is a basic card it'll always be one, but getFilterAmt handles the proper logic of returning a value
        }
        return filteredItems;
    }

    public List<FluidStack> getFilteredFluids() {
        List<FluidStack> filteredFluids = new ArrayList<>();
        ItemStackHandler filterSlotHandler;
        if (filterCard.getItem() instanceof FilterBasic)
            filterSlotHandler = FilterBasic.getInventory(filterCard);
        else
            filterSlotHandler = FilterCount.getInventory(filterCard);
        for (int i = 0; i < filterSlotHandler.getSlots(); i++) {
            ItemStack itemStack = filterSlotHandler.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                LazyOptional<IFluidHandlerItem> fluidHandlerOptional = FluidUtil.getFluidHandler(itemStack);
                if (!fluidHandlerOptional.isPresent()) continue;
                IFluidHandler fluidHandler = fluidHandlerOptional.resolve().get();
                for (int tank = 0; tank < fluidHandler.getTanks(); tank++) {
                    FluidStack fluidStack = fluidHandler.getFluidInTank(tank);
                    if (!fluidStack.isEmpty())
                        filteredFluids.add(fluidStack); //If this is a basic card it'll always be one, but getFilterAmt handles the proper logic of returning a value
                }
            }
        }
        return filteredFluids;
    }

    public List<String> getFilterTags() {
        if (filterCard.getItem() instanceof FilterTag) {
            return FilterTag.getTags(filterCard);
        }
        return new ArrayList<>();
    }

    public List<String> getFilterNBTs() {
        if (filterCard.getItem() instanceof FilterNBT) {
            return FilterTag.getTags(filterCard);
        }
        return new ArrayList<>();
    }

    public boolean isStackValidForCard(ItemStack testStack) {
        if (filterCard.isEmpty()) return true; //If theres no filter in the card
        lookupKey.set(testStack, isCompareNBT);
        Boolean cachedResult = filterCache.get(lookupKey);
        if (cachedResult != null) return cachedResult;

        if (filterCard.getItem() instanceof FilterMod) {
            String modId = testStack.getItem().getCreatorModId(testStack);
            for (ItemStack stack : filteredItems) {
                if (stack.getItem().getCreatorModId(stack).equals(modId)) {
                    ItemStackKey key = new ItemStackKey(testStack, isCompareNBT);
                    filterCache.put(key, isAllowList);
                    return isAllowList;
                }
            }
        } else if (filterCard.getItem() instanceof FilterTag) {
            List<TagKey<net.minecraft.world.item.Item>> tags = testStack.getItem().builtInRegistryHolder().tags().toList();
            for (TagKey<net.minecraft.world.item.Item> tagKey : tags) {
                String tag = tagKey.location().toString().toLowerCase(Locale.ROOT);
                if (filterTags.contains(tag)) {
                    ItemStackKey key = new ItemStackKey(testStack, isCompareNBT);
                    filterCache.put(key, isAllowList);
                    return isAllowList;
                }
            }
        } else if (filterCard.getItem() instanceof FilterNBT) {
            if (testStack.hasTag()) {
                for (String tag : testStack.getTag().getAllKeys()) {
                    if (filterNBTs.contains(tag)) {
                        ItemStackKey key = new ItemStackKey(testStack, isCompareNBT);
                        filterCache.put(key, isAllowList);
                        return isAllowList;
                    }
                }
            }
        } else {
            for (ItemStackKey keyInFilter : filteredItemsKeys) {
                if (keyInFilter.equals(lookupKey)) {
                    ItemStackKey key = new ItemStackKey(testStack, isCompareNBT);
                    filterCache.put(key, isAllowList);
                    return isAllowList;
                }
            }
        }
        ItemStackKey key = new ItemStackKey(testStack, isCompareNBT);
        filterCache.put(key, !isAllowList);
        return !isAllowList;
    }

    public boolean isStackValidForCard(FluidStack testStack) {
        if (filterCard.isEmpty()) return true; //If theres no filter in the card
        lookupKeyFluid.set(testStack, isCompareNBT);
        Boolean cachedResult = filterCacheFluid.get(lookupKeyFluid);
        if (cachedResult != null) return cachedResult;

        if (filterCard.getItem() instanceof FilterMod) {
            String modId = ForgeRegistries.FLUIDS.getKey(testStack.getFluid()).getNamespace();
            for (FluidStack stack : filteredFluids) {
                if (ForgeRegistries.FLUIDS.getKey(stack.getFluid()).getNamespace().equals(modId)) {
                    FluidStackKey key = new FluidStackKey(testStack, isCompareNBT);
                    filterCacheFluid.put(key, isAllowList);
                    return isAllowList;
                }
            }
        } else if (filterCard.getItem() instanceof FilterTag) {
            for (TagKey tagKey : testStack.getFluid().builtInRegistryHolder().tags().toList()) {
                String tag = tagKey.location().toString().toLowerCase(Locale.ROOT);
                if (filterTags.contains(tag)) {
                    FluidStackKey key = new FluidStackKey(testStack, isCompareNBT);
                    filterCacheFluid.put(key, isAllowList);
                    return isAllowList;
                }
            }
        } else {
            for (FluidStackKey keyInFilter : filteredFluidsKeys) {
                if (keyInFilter.equals(lookupKeyFluid)) {
                    FluidStackKey key = new FluidStackKey(testStack, isCompareNBT);
                    filterCacheFluid.put(key, isAllowList);
                    return isAllowList;
                }
            }
        }
        FluidStackKey key = new FluidStackKey(testStack, isCompareNBT);
        filterCacheFluid.put(key, !isAllowList);
        return !isAllowList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseCardCache that) {
            return that.be.getBlockPos().equals(this.be.getBlockPos()) &&
                   Objects.equals(that.be.getLevel().dimension(), this.be.getLevel().dimension()) &&
                   that.direction == this.direction &&
                   that.cardSlot == this.cardSlot;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(be.getBlockPos(), be.getLevel().dimension(), direction, cardSlot);
    }
}