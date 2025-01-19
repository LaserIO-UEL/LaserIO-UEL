package com.direwolf20.laserio.common.network.packets;

import com.direwolf20.laserio.common.containers.CardEnergyContainer;
import com.direwolf20.laserio.common.containers.CardHolderContainer;
import com.direwolf20.laserio.common.containers.LaserNodeContainer;
import com.direwolf20.laserio.common.containers.customhandler.CardItemHandler;
import com.direwolf20.laserio.common.items.CardCloner;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.cards.CardEnergy;
import com.direwolf20.laserio.common.items.cards.CardRedstone;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketCopyPasteCard {
    int slot;
    boolean copy;

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

    public static void playSound(ServerPlayer player, Holder<SoundEvent> soundEventHolder) {
        // Get player's position
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        // Create the packet
        ClientboundSoundPacket packet = new ClientboundSoundPacket(
                soundEventHolder, // The sound event
                SoundSource.MASTER, // The sound category
                x, y, z, // The sound location
                1, // The volume, 1 is normal, higher is louder
                1, // The pitch, 1 is normal, higher is higher pitch
                1 // A random for some reason? (Some sounds have different variants, like the enchanting table success
        );

        // Send the packet to the player
        player.connection.send(packet);
    }

    public static int returnItemToHolder(LaserNodeContainer container, ItemStack returnStack, boolean simulate) {
        int returnCount = returnStack.getCount();
        if (returnCount == 0) {
            return 0;
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
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;

                AbstractContainerMenu container = player.containerMenu;
                if (container == null)
                    return;

                if (!(container instanceof LaserNodeContainer))
                    return;

                LaserNodeContainer laserNodeContainer = (LaserNodeContainer) container;

                if (player.containerMenu.getCarried().isEmpty())
                    return;

                ItemStack slotStack = container.getSlot(msg.slot).getItem();
                ItemStack clonerStack = container.getCarried();
                if (msg.copy) { //copy mode
                    CardCloner.setItemType(clonerStack, slotStack.getItem().toString());
                    CompoundTag compoundTag = slotStack.getTag() == null ? new CompoundTag() : slotStack.getTag();
                    CardCloner.saveSettings(clonerStack, compoundTag);
                    playSound(player, Holder.direct(SoundEvent.createVariableRangeEvent(new ResourceLocation(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT.getLocation().toString()))));
                } else {
                    Item slotItem = slotStack.getItem();
                    if (slotItem.toString().equals(CardCloner.getItemType(clonerStack))) {
                        ItemStack filterNeeded = CardCloner.getFilter(clonerStack);
                        ItemStack overclockersNeeded = CardCloner.getOverclocker(clonerStack);
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
                        if (!existingFilter.is(filterNeeded.getItem())) {
                            filterSatisfied = getItemFromHolder(laserNodeContainer, filterNeeded, true);
                        }
                        int amtReturn = 0;
                        int amtNeeded = 0;
                        if (!existingOverclockers.is(overclockersNeeded.getItem())) {
                            amtReturn = existingOverclockers.getCount();
                            amtNeeded = overclockersNeeded.getCount();
                        } else {
                            int amt = existingOverclockers.getCount() - overclockersNeeded.getCount();
                            if (amt > 0) {
                                amtReturn = amt;
                            } else {
                                amtNeeded = -amt;
                            }
                        }
                        boolean overclockSatisfied = true;
                        if (amtNeeded > 0) {
                            ItemStack neededStack = new ItemStack(overclockersNeeded.getItem(), amtNeeded);
                            overclockSatisfied = getItemFromHolder(laserNodeContainer, neededStack, true);
                        }
                        if (filterSatisfied && overclockSatisfied) {
                            if (!existingFilter.is(filterNeeded.getItem())) {
                                if (returnItemToHolder(laserNodeContainer, existingFilter, false) != 0) {
                                    //Drop item in world
                                    ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), existingFilter);
                                    player.level().addFreshEntity(itemEntity);
                                }
                                getItemFromHolder(laserNodeContainer, filterNeeded, false);
                            }
                            if (amtReturn > 0) {
                                ItemStack returnStack = new ItemStack(existingOverclockers.getItem(), amtReturn);
                                int remaining = returnItemToHolder(laserNodeContainer, returnStack, false);
                                if (remaining > 0) {
                                    //Drop item in world
                                    returnStack.setCount(remaining);
                                    ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), returnStack);
                                    player.level().addFreshEntity(itemEntity);
                                }
                            }
                            if (amtNeeded > 0) {
                                ItemStack neededStack = new ItemStack(overclockersNeeded.getItem(), amtNeeded);
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
                            playSound(player, Holder.direct(SoundEvent.createVariableRangeEvent(new ResourceLocation(SoundEvents.ENCHANTMENT_TABLE_USE.getLocation().toString()))));
                            ((LaserNodeContainer)container).tile.updateThisNode();
                        } else {
                            playSound(player, Holder.direct(SoundEvent.createVariableRangeEvent(new ResourceLocation(SoundEvents.WAXED_SIGN_INTERACT_FAIL.getLocation().toString()))));
                        }
                    }
                    else {
                        playSound(player, Holder.direct(SoundEvent.createVariableRangeEvent(new ResourceLocation(SoundEvents.WAXED_SIGN_INTERACT_FAIL.getLocation().toString()))));
                    }
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }
}