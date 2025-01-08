package com.direwolf20.laserio.common.containers;

import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.blocks.LaserNode;
import com.direwolf20.laserio.common.containers.customhandler.CardItemHandler;
import com.direwolf20.laserio.common.containers.customslot.CardHolderSlot;
import com.direwolf20.laserio.common.containers.customslot.CardOverclockSlot;
import com.direwolf20.laserio.common.items.CardHolder;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.cards.CardEnergy;
import com.direwolf20.laserio.common.items.upgrades.OverclockerCard;
import com.direwolf20.laserio.setup.Config;
import com.direwolf20.laserio.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

import java.util.UUID;

public class CardEnergyContainer extends AbstractContainerMenu {
    public static final int SLOTS = (Config.MAX_FE_TIERS.get().isEmpty()) ? 0 : 1;
    public CardItemHandler handler;
    public ItemStack cardItem;
    public Player playerEntity;
    protected IItemHandler playerInventory;
    public BlockPos sourceContainer = BlockPos.ZERO;
    public byte direction = -1;
    public ItemStack cardHolder;
    public IItemHandler cardHolderHandler;
    public UUID cardHolderUUID;

    protected CardEnergyContainer(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    public CardEnergyContainer(int windowId, Inventory playerInventory, Player player, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, player, extraData.readItem());
        this.direction = extraData.readByte();
        if (SLOTS == 1) {
            this.cardHolder = LaserNode.findCardHolders(player);
        }
    }

    public CardEnergyContainer(int windowId, Inventory playerInventory, Player player, ItemStack cardItem) {
        super(Registration.CardEnergy_Container.get(), windowId);
        this.playerEntity = player;
        if (SLOTS == 1) {
            this.handler = CardEnergy.getInventory(cardItem);
            this.cardHolder = LaserNode.findCardHolders(player);
        }
        this.playerInventory = new InvWrapper(playerInventory);
        this.cardItem = cardItem;
        if (handler != null) {
            addSlotRange(handler, 0, 153, 5, 1, 18);
        }
        if (cardHolder != null && !cardHolder.isEmpty()) {
            this.cardHolderHandler = cardHolder.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(new ItemStackHandler(CardHolderContainer.SLOTS));
            addSlotBox(cardHolderHandler, 0, -92, 32, 5, 18, 3, 18);
            cardHolderUUID = CardHolder.getUUID(cardHolder);
        }
        layoutPlayerInventorySlots(8, 84);
    }

    public CardEnergyContainer(int windowId, Inventory playerInventory, Player player, BlockPos sourcePos, ItemStack cardItem, byte direction) {
        this(windowId, playerInventory, player, cardItem);
        this.sourceContainer = sourcePos;
        this.direction = direction;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId >= 0) {
            Slot slot = slots.get(slotId);
            ItemStack stackInSlot = slot.getItem();
            Item itemInSlot = stackInSlot.getItem();
            if (slot instanceof CardHolderSlot) {
                if (itemInSlot instanceof BaseCard) {
                    return;
                }
                ItemStack carriedItem = getCarried();
                if (stackInSlot.getMaxStackSize() == 1 && stackInSlot.getCount() > 1) {
                    if (!carriedItem.isEmpty() && !stackInSlot.isEmpty() && !ItemStack.isSameItemSameTags(carriedItem, stackInSlot)) {
                        return;
                    }
                }
            } else if ((itemInSlot instanceof BaseCard && stackInSlot == player.getMainHandItem()) || (itemInSlot instanceof CardHolder && (SLOTS == 1 || stackInSlot == player.getMainHandItem()))) {
                return;
            }
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        if (SLOTS == 1 && cardHolder.isEmpty() && cardHolderUUID != null) {
            //System.out.println("Lost card holder!");
            Inventory playerInventory = playerEntity.getInventory();
            for (int i = 0; i < playerInventory.items.size(); i++) {
                ItemStack itemStack = playerInventory.items.get(i);
                if (itemStack.getItem() instanceof CardHolder) {
                    if (CardHolder.getUUID(itemStack).equals(cardHolderUUID)) {
                        cardHolder = itemStack;
                        break;
                    }
                }
            }
        }
        if (sourceContainer.equals(BlockPos.ZERO)) {
            return playerIn.getMainHandItem().equals(cardItem) || playerIn.getOffhandItem().equals(cardItem);
        }
        return true;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        if (slot instanceof CardHolderSlot) {
            return false;
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        if (SLOTS == 0 || cardItem.getCount() > 1) return ItemStack.EMPTY; //Don't let quickMove happen in multistack cards or if Energy Overclockers are not defined
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemStack = stack.copy();
            if (ItemHandlerHelper.canItemStacksStack(itemStack, cardItem)) return ItemStack.EMPTY;

            if (index < SLOTS) {
                if (!cardHolder.isEmpty()) { //Do the below set of logic if we have a card holder, otherwise just try to move to inventory
                    if (!this.moveItemStackTo(stack, SLOTS, SLOTS + CardHolderContainer.SLOTS, false)) { //Try the CardHolder First!
                        return ItemStack.EMPTY;
                    }
                    if (!this.moveItemStackTo(stack, SLOTS + CardHolderContainer.SLOTS, 36 + SLOTS + CardHolderContainer.SLOTS, true)) {
                        return ItemStack.EMPTY;
                    }
                } else { //If no card holder, the slot targets are different
                    if (!this.moveItemStackTo(stack, SLOTS, 36 + SLOTS, true)) {
                        return ItemStack.EMPTY;
                    }
                }
                slot.onQuickCraft(stack, itemStack);
            } else { //From player inventory (or Card Holder) TO something
                ItemStack currentStack = slot.getItem().copy();
                if (slots.get(0).mayPlace(currentStack) && currentStack.getItem() instanceof OverclockerCard card && card.getEnergyTier() > 0) {
                    if (!this.moveItemStackTo(stack, 0, SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (!playerIn.level().isClientSide()) {
                    CardEnergy.setInventory(cardItem, handler);
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }
        return itemStack;
    }

    protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            if (handler instanceof CardItemHandler && index == 0)
                addSlot(new CardOverclockSlot(handler, index, x, y));
            else if (handler != null && (handler.getSlots() == CardHolderContainer.SLOTS))
                addSlot(new CardHolderSlot(handler, index, x, y));
            else
                addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public void removed(Player playerIn) {
        Level world = playerIn.level();
        if (!world.isClientSide) {
            if (SLOTS == 1)
                CardEnergy.setInventory(cardItem, handler);
            if (!sourceContainer.equals(BlockPos.ZERO)) {
                BlockEntity blockEntity = world.getBlockEntity(sourceContainer);
                if (blockEntity instanceof LaserNodeBE)
                    ((LaserNodeBE) blockEntity).updateThisNode();
            }
        }
        super.removed(playerIn);
    }
}