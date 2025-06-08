package com.direwolf20.laserio.common.network.packets;

import com.direwolf20.laserio.common.containers.CardEnergyContainer;
import com.direwolf20.laserio.common.containers.CardHolderContainer;
import com.direwolf20.laserio.common.containers.LaserNodeContainer;
import com.direwolf20.laserio.common.containers.customhandler.CardItemHandler;
import com.direwolf20.laserio.common.items.CardCloner;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.cards.CardEnergy;
import com.direwolf20.laserio.common.items.cards.CardRedstone;
import com.direwolf20.laserio.util.SoundUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketCopyPasteCard {
    private int slot;
    private boolean copy;

    public PacketCopyPasteCard(int slot, boolean copy) {
        this.slot = slot;
        this.copy = copy;
    }

    public static void encode(PacketCopyPasteCard msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.slot);
        buffer.writeBoolean(msg.copy);
    }

    public static PacketCopyPasteCard decode(FriendlyByteBuf buffer) {
        return new PacketCopyPasteCard(buffer.readInt(), buffer.readBoolean());
    }

    public static int returnItemToHolder(LaserNodeContainer container, ItemStack returnStack, boolean simulate) {
        int returnCount = returnStack.getCount();
        if (returnCount == 0) {
            return 0;
        }
        if (container.cardHolder.isEmpty()) {
            return returnCount;
        }
        Map<Integer, Integer> returnStackMap = new HashMap<>();
        for (int returnSlot = LaserNodeContainer.SLOTS; returnSlot < (LaserNodeContainer.SLOTS + CardHolderContainer.SLOTS); returnSlot++) {
            ItemStack possibleReturnStack = container.getSlot(returnSlot).getItem();
            if (possibleReturnStack.isEmpty() || (possibleReturnStack.is(returnStack.getItem()) && possibleReturnStack.getCount() < possibleReturnStack.getMaxStackSize())) {
                int roomAvailable = possibleReturnStack.getMaxStackSize() - possibleReturnStack.getCount();
                int amtFit = Math.min(returnCount, roomAvailable);
                returnStackMap.put(returnSlot, amtFit);
                returnCount -= amtFit;
                if (returnCount == 0) {
                    break;
                }
            }
        }
        if (simulate) { //Return the remaining
            return returnCount;
        }
        for (Map.Entry<Integer, Integer> entry : returnStackMap.entrySet()) {
            ItemStack possibleReturnStack = container.getSlot(entry.getKey()).getItem();
            if (possibleReturnStack.isEmpty()) {
                container.getSlot(entry.getKey()).set(returnStack);
                //In *THEORY* this should never be needed but who knows!
                possibleReturnStack = container.getSlot(entry.getKey()).getItem();
                possibleReturnStack.setCount(entry.getValue());
            } else {
                possibleReturnStack.grow(entry.getValue());
            }
        }
        return returnCount; //Since we got here we can assume we updated everything
    }

    public static boolean getItemFromHolder(LaserNodeContainer container, ItemStack neededStack, boolean simulate) {
        int neededCount = neededStack.getCount();
        if (neededCount == 0) {
            return true;
        }
        if (container.cardHolder.isEmpty()) {
            return false;
        }
        Map<Integer, Integer> foundStackMap = new HashMap<>();
        for (int getSlot = LaserNodeContainer.SLOTS; getSlot < (LaserNodeContainer.SLOTS + CardHolderContainer.SLOTS); getSlot++) {
            ItemStack possibleStack = container.getSlot(getSlot).getItem();
            if (possibleStack.is(neededStack.getItem())) {
                int stackAvailable = possibleStack.getCount();
                int amtFound = Math.min(neededCount, stackAvailable);
                foundStackMap.put(getSlot, amtFound);
                neededCount -= amtFound;
                if (neededCount == 0) {
                    if (simulate) {
                        return true;
                    }
                    break;
                }
            }
        }
        if (neededCount > 0) { //If we didn't find everything we needed to
            return false;
        }
        for (Map.Entry<Integer, Integer> entry : foundStackMap.entrySet()) {
            ItemStack foundStack = container.getSlot(entry.getKey()).getItem();
            foundStack.shrink(entry.getValue());
        }
        return true; //Since we got here we can assume we updated everything
    }

    public static class Handler {
        public static void handle(PacketCopyPasteCard msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer sender = ctx.get().getSender();
                if (sender == null) {
                    return;
                }
                AbstractContainerMenu container = sender.containerMenu;
                if (!(container instanceof LaserNodeContainer)) {
                    return;
                }
                LaserNodeContainer laserNodeContainer = (LaserNodeContainer) container;
                ItemStack clonerStack = container.getCarried();
                if (!(clonerStack.getItem() instanceof CardCloner)) {
                    return;
                }
                ItemStack slotStack = container.getSlot(msg.slot).getItem();
                if (!(slotStack.getItem() instanceof BaseCard)) {
                    return;
                }
                if (msg.copy) { //Copy mode
                    CardCloner.setItemType(clonerStack, slotStack.getItem().toString());
                    CompoundTag compoundTag = slotStack.getTag() == null ? new CompoundTag() : slotStack.getTag();
                    CardCloner.setSettings(clonerStack, compoundTag);
                    SoundUtil.playSound(sender, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);
                } else { //Paste mode
                    Item slotItem = slotStack.getItem();
                    if (slotItem.toString().equals(CardCloner.getItemType(clonerStack))) {
                        ItemStack neededFilter = CardCloner.getCopiedCardFilter(clonerStack);
                        ItemStack neededOverclockers = CardCloner.getCopiedCardOverclocker(clonerStack);
                        ItemStack existingFilter = ItemStack.EMPTY;
                        ItemStack existingOverclockers = ItemStack.EMPTY;
                        if (slotItem instanceof CardEnergy && CardEnergyContainer.SLOTS == 1) {
                            CardItemHandler cardItemHandler = CardEnergy.getInventory(slotStack);
                            existingOverclockers = cardItemHandler.getStackInSlot(0);
                        } else if (!(slotItem instanceof CardRedstone)) {
                            CardItemHandler cardItemHandler = BaseCard.getInventory(slotStack);
                            existingFilter = cardItemHandler.getStackInSlot(0);
                            existingOverclockers = cardItemHandler.getStackInSlot(1);
                        }
                        boolean filterSatisfied = true;
                        if (!existingFilter.is(neededFilter.getItem())) {
                            filterSatisfied = getItemFromHolder(laserNodeContainer, neededFilter, true);
                        }
                        int returnAmt = 0;
                        int neededAmt = 0;
                        if (!existingOverclockers.is(neededOverclockers.getItem())) {
                            returnAmt = existingOverclockers.getCount();
                            neededAmt = neededOverclockers.getCount();
                        } else {
                            int amt = existingOverclockers.getCount() - neededOverclockers.getCount();
                            if (amt > 0) {
                                returnAmt = amt;
                            } else {
                                neededAmt = -amt;
                            }
                        }
                        boolean overclockSatisfied = true;
                        if (neededAmt > 0) {
                            ItemStack neededStack = new ItemStack(neededOverclockers.getItem(), neededAmt);
                            overclockSatisfied = getItemFromHolder(laserNodeContainer, neededStack, true);
                        }
                        if (filterSatisfied && overclockSatisfied) {
                            if (!existingFilter.is(neededFilter.getItem())) {
                                if (returnItemToHolder(laserNodeContainer, existingFilter, false) != 0) {
                                    //Drop item in world
                                    ItemEntity itemEntity = new ItemEntity(sender.level(), sender.getX(), sender.getY(), sender.getZ(), existingFilter);
                                    sender.level().addFreshEntity(itemEntity);
                                }
                                getItemFromHolder(laserNodeContainer, neededFilter, false);
                            }
                            if (returnAmt > 0) {
                                ItemStack returnStack = new ItemStack(existingOverclockers.getItem(), returnAmt);
                                int remaining = returnItemToHolder(laserNodeContainer, returnStack, false);
                                if (remaining > 0) {
                                    //Drop item in world
                                    returnStack.setCount(remaining);
                                    ItemEntity itemEntity = new ItemEntity(sender.level(), sender.getX(), sender.getY(), sender.getZ(), returnStack);
                                    sender.level().addFreshEntity(itemEntity);
                                }
                            }
                            if (neededAmt > 0) {
                                ItemStack neededStack = new ItemStack(neededOverclockers.getItem(), neededAmt);
                                getItemFromHolder(laserNodeContainer, neededStack, false);
                            }
                            ItemStack tempStack = slotStack.copy();
                            CompoundTag compoundTag = CardCloner.getSettings(clonerStack);
                            if (compoundTag.isEmpty()) {
                                tempStack.setTag(null);
                            } else {
                                tempStack.setTag(compoundTag.copy());
                            }
                            container.getSlot(msg.slot).set(tempStack);
                            SoundUtil.playSound(sender, SoundEvents.ENCHANTMENT_TABLE_USE);
                            ((LaserNodeContainer)container).tile.updateThisNode();
                        } else {
                            SoundUtil.playSound(sender, SoundEvents.WAXED_SIGN_INTERACT_FAIL);
                        }
                    } else {
                        SoundUtil.playSound(sender, SoundEvents.WAXED_SIGN_INTERACT_FAIL);
                    }
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }
}