package com.direwolf20.laserio.common.items;

import com.direwolf20.laserio.client.blockentityrenders.LaserNodeBERender;
import com.direwolf20.laserio.common.containers.CardEnergyContainer;
import com.direwolf20.laserio.common.containers.CardItemContainer;
import com.direwolf20.laserio.common.items.cards.BaseCard;
import com.direwolf20.laserio.common.items.cards.BaseCard.TransferMode;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import java.util.List;

import static com.direwolf20.laserio.util.MiscTools.tooltipMaker;

public class CardCloner extends Item {
    public CardCloner() {
        super(new Properties()
                .stacksTo(1));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        Minecraft mc = Minecraft.getInstance();
        if (world == null || mc.player == null) {
            return;
        }

        boolean sneakPressed = Screen.hasShiftDown();

        if (!sneakPressed) {
            tooltip.add(tooltipMaker("laserio.tooltip.item.show_settings", ChatFormatting.GRAY));
        } else {
            String cardType = getItemType(stack);
            boolean isEnergyCard = false;
            boolean isRedstoneCard = false;
            MutableComponent toWrite = tooltipMaker("laserio.tooltip.item.filter.type", ChatFormatting.GRAY);
            ChatFormatting cardColor = switch(cardType) {
                case "card_item" -> ChatFormatting.GREEN;
                case "card_fluid" -> ChatFormatting.BLUE;
                case "card_energy" -> {
                    isEnergyCard = true;
                    yield ChatFormatting.YELLOW;
                }
                case "card_redstone" -> {
                    isRedstoneCard = true;
                    yield ChatFormatting.RED;
                }
                case "card_chemical" -> ChatFormatting.LIGHT_PURPLE;
                default -> ChatFormatting.WHITE;
            };
            if (cardType.equals(""))
                toWrite.append(tooltipMaker("laserio.tooltip.item.card.None", cardColor));
            else
                toWrite.append(tooltipMaker("item.laserio." + cardType, cardColor));
            tooltip.add(toWrite);
            if (cardType.equals("")) {
                return;
            }

            CompoundTag compoundTag = stack.getOrCreateTag().getCompound("settings");
            int mode = !compoundTag.contains("mode") ? 0 : compoundTag.getByte("mode");
            TransferMode currentMode = BaseCard.TransferMode.values()[mode];
            toWrite = tooltipMaker("laserio.tooltip.item.card.mode", ChatFormatting.GRAY);
            ChatFormatting modeColor = switch(currentMode) {
                case EXTRACT -> ChatFormatting.RED;
                case INSERT -> ChatFormatting.GREEN;
                case STOCK -> ChatFormatting.BLUE;
                case SENSOR -> ChatFormatting.YELLOW;
                default -> ChatFormatting.GRAY;
            };
            toWrite.append(tooltipMaker("laserio.tooltip.item.card.mode." + currentMode, modeColor));
            tooltip.add(toWrite);

            toWrite = tooltipMaker("laserio.tooltip.item.card.channel", ChatFormatting.GRAY);
            int channel = !compoundTag.contains("channel") ? 0 : compoundTag.getByte("channel");
            toWrite.append(tooltipMaker(String.valueOf(channel), LaserNodeBERender.COLORS[channel].getRGB()));
            tooltip.add(toWrite);
            if (isRedstoneCard) {
                return;
            }

            if (!isEnergyCard) {
                toWrite = tooltipMaker("laserio.tooltip.item.card.Filter", ChatFormatting.GRAY);
                ItemStack filterStack = getFilter(stack);
                if (filterStack.isEmpty())
                    toWrite.append(tooltipMaker("laserio.tooltip.item.card.None", ChatFormatting.WHITE));
                else
                    toWrite.append(tooltipMaker("item.laserio." + filterStack.getItem(), ChatFormatting.DARK_AQUA));
                tooltip.add(toWrite);
            }

            if (!isEnergyCard || (isEnergyCard && CardEnergyContainer.SLOTS == 1)) {
                ItemStack overclockerStack = getOverclocker(stack);
                if (isEnergyCard) {
                    toWrite = tooltipMaker("laserio.tooltip.item.card.Overclocker", ChatFormatting.GRAY);
                    if (overclockerStack.isEmpty())
                        toWrite.append(tooltipMaker("laserio.tooltip.item.card.None", ChatFormatting.WHITE));
                    else
                        toWrite.append(tooltipMaker("item.laserio." + overclockerStack.getItem(), ChatFormatting.DARK_AQUA));
                } else {
                    toWrite = tooltipMaker("laserio.tooltip.item.card.Overclockers", ChatFormatting.GRAY);
                    if (overclockerStack.isEmpty())
                        toWrite.append(tooltipMaker(String.valueOf(0), ChatFormatting.WHITE));
                    else
                        toWrite.append(tooltipMaker(String.valueOf(overclockerStack.getCount()), ChatFormatting.DARK_AQUA));
                }
                tooltip.add(toWrite);
            }
        }
    }

    public static void setItemType(ItemStack stack, String itemType) {
        stack.getOrCreateTag().putString("itemType", itemType);
    }

    public static String getItemType(ItemStack stack) {
        return stack.getOrCreateTag().getString("itemType");
    }

    public static void saveSettings(ItemStack stack, CompoundTag tag) {
        stack.getOrCreateTag().put("settings", tag);
    }

    public static CompoundTag getSettings(ItemStack stack) {
        return stack.getOrCreateTag().getCompound("settings");
    }

    public static ItemStack getFilter(ItemStack stack) {
        String cardType = getItemType(stack);
        CompoundTag compoundTag = getSettings(stack);
        ItemStack filterStack = ItemStack.EMPTY;
        if (!cardType.equals("card_energy") && !cardType.equals("card_redstone")) {
            ItemStackHandler itemStackHandler = new ItemStackHandler(CardItemContainer.SLOTS);
            itemStackHandler.deserializeNBT(compoundTag.getCompound("inv"));
            filterStack = itemStackHandler.getStackInSlot(0);
        }
        return filterStack;
    }

    public static ItemStack getOverclocker(ItemStack stack) {
        String cardType = getItemType(stack);
        CompoundTag compoundTag = getSettings(stack);
        ItemStack overclockStack = ItemStack.EMPTY;
        if (cardType.equals("card_energy")) {
            if (CardEnergyContainer.SLOTS == 1) {
                ItemStackHandler itemStackHandler = new ItemStackHandler(CardEnergyContainer.SLOTS);
                itemStackHandler.deserializeNBT(compoundTag.getCompound("inv"));
                overclockStack = itemStackHandler.getStackInSlot(0);
            }
        } else if (!cardType.equals("card_redstone")) {
            ItemStackHandler itemStackHandler = new ItemStackHandler(CardItemContainer.SLOTS);
            itemStackHandler.deserializeNBT(compoundTag.getCompound("inv"));
            overclockStack = itemStackHandler.getStackInSlot(1);
        }
        return overclockStack;
    }
}