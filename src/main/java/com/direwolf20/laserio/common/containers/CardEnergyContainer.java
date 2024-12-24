package com.direwolf20.laserio.common.containers;

import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.containers.customhandler.CardItemHandler;
import com.direwolf20.laserio.common.containers.customslot.CardOverclockSlot;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class CardEnergyContainer extends AbstractContainerMenu {
    public static final int SLOTS = (Config.MAX_FE_TIERS.get().isEmpty()) ? 0 : 1;
    public CardItemHandler handler;
    public ItemStack cardItem;
    public Player playerEntity;
    protected IItemHandler playerInventory;
    public BlockPos sourceContainer = BlockPos.ZERO;
    public byte direction = -1;

    protected CardEnergyContainer(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    public CardEnergyContainer(int windowId, Inventory playerInventory, Player player, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, player, extraData.readItem());
        this.direction = extraData.readByte();
    }

    public CardEnergyContainer(int windowId, Inventory playerInventory, Player player, ItemStack cardItem) {
        super(Registration.CardEnergy_Container.get(), windowId);
        playerEntity = player;
        if (SLOTS == 1)
            this.handler = CardEnergy.getInventory(cardItem);
        this.playerInventory = new InvWrapper(playerInventory);
        this.cardItem = cardItem;
        if (handler != null) {
            addSlotRange(handler, 0, 153, 5, 1, 18);
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
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        if (sourceContainer.equals(BlockPos.ZERO))
            return playerIn.getMainHandItem().equals(cardItem) || playerIn.getOffhandItem().equals(cardItem);
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        if (cardItem.getCount() > 1) return ItemStack.EMPTY; // Don't let quickMove happen in multistack cards
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (ItemHandlerHelper.canItemStacksStack(itemstack, cardItem)) return ItemStack.EMPTY;

            if (SLOTS == 1) {
                if (index < SLOTS) {
                    if (!this.moveItemStackTo(stack, SLOTS, 36 + SLOTS, true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(stack, itemstack);
                } else { //From player inventory TO something
                    ItemStack currentStack = slot.getItem().copy();
                    if (slots.get(0).mayPlace(currentStack) && currentStack.getItem() instanceof OverclockerCard card && card.getEnergyTier() > 0) {
                        if (!this.moveItemStackTo(stack, 0, SLOTS, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    if (!playerIn.level().isClientSide())
                        CardEnergy.setInventory(cardItem, handler);
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

    protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            if (handler instanceof CardItemHandler && index == 0)
                addSlot(new CardOverclockSlot(handler, index, x, y));
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