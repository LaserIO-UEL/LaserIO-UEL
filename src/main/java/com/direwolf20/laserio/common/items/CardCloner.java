package com.direwolf20.laserio.common.items;

import com.direwolf20.laserio.client.blockentityrenders.LaserNodeBERender;
import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.blocks.LaserNode;
import com.direwolf20.laserio.common.containers.CardEnergyContainer;
import com.direwolf20.laserio.common.containers.CardItemContainer;
import com.direwolf20.laserio.common.containers.LaserNodeContainer;
import com.direwolf20.laserio.common.items.cards.BaseCard.TransferMode;
import com.direwolf20.laserio.setup.Config;
import com.direwolf20.laserio.util.ItemHandlerUtil.InventoryCardCounts;
import com.direwolf20.laserio.util.SoundUtil;
import com.direwolf20.laserio.util.VectorHelper;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import java.awt.Color;
import java.util.List;

import static com.direwolf20.laserio.util.MiscTools.tooltipMaker;
import static com.direwolf20.laserio.util.MiscTools.tooltipMakerLiteral;

public class CardCloner extends Item {
    public static final MutableComponent[] PASTE_MODE_MESSAGES = {
            Component.translatable("message.laserio.card_cloner.paste_mode").append(Component.translatable("message.laserio.card_cloner.paste_mode.node_contents")),
            Component.translatable("message.laserio.card_cloner.paste_mode").append(Component.translatable("message.laserio.card_cloner.paste_mode.network_settings"))
    };

    public CardCloner() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack cardCloner = player.getItemInHand(hand);
        BlockHitResult lookingAt = VectorHelper.getLookingAt(player, ClipContext.Fluid.NONE, Config.MAX_INTERACTION_RANGE.get());
        if (lookingAt == null || !(level.getBlockState(lookingAt.getBlockPos()).getBlock() instanceof LaserNode)) {
            if (level.isClientSide()) {
                if (player.isShiftKeyDown()) {
                    player.displayClientMessage(Component.translatable("message.laserio.card_cloner.stored_settings_cleared"), true);
                    player.playSound(SoundEvents.BUCKET_EMPTY);
                } else {
                    player.displayClientMessage(PASTE_MODE_MESSAGES[getPasteNetworkSettings(cardCloner) ? 0 : 1], true);
                    player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
                }
                return InteractionResultHolder.pass(cardCloner);
            } else {
                if (player.isShiftKeyDown()) {
                    CompoundTag cardClonerTag = cardCloner.getOrCreateTag();
                    cardClonerTag.remove("itemType");
                    cardClonerTag.remove("settings");
                    cardClonerTag.remove("nodeData");
                } else {
                    setPasteNetworkSettings(cardCloner, !getPasteNetworkSettings(cardCloner));
                }
                return InteractionResultHolder.pass(cardCloner);
            }
        }
        if (level.isClientSide()) {
            return InteractionResultHolder.success(cardCloner);
        }
        BlockPos targetPos = lookingAt.getBlockPos();
        BlockEntity targetBE = level.getBlockEntity(targetPos);
        if (!(targetBE instanceof LaserNodeBE)) {
            return InteractionResultHolder.pass(cardCloner);
        }
        LaserNodeBE laserNodeBE = (LaserNodeBE) targetBE;
        if (player.isShiftKeyDown()) {
            CompoundTag nodeTag = new CompoundTag();
            laserNodeBE.saveAdditional(nodeTag);
            String dimensionName = level.dimension().location().toShortLanguageKey();
            nodeTag.putString("dimension", dimensionName);
            setNodeData(cardCloner, nodeTag);
            player.displayClientMessage(Component.translatable("message.laserio.card_cloner.node_copied"), true);
            SoundUtil.playSound(player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);
        } else {
            CompoundTag nodeTag = getNodeData(cardCloner);
            if (!nodeTag.isEmpty()) {
                if (getPasteNetworkSettings(cardCloner)) {
                    Color laserColor = new Color(nodeTag.getInt("laserColor"), true);
                    laserNodeBE.setColor(laserColor, nodeTag.getInt("wrenchAlpha"));
                    laserNodeBE.discoverAllNodes();
                    player.displayClientMessage(Component.translatable("message.laserio.card_cloner.network_settings_pasted"), true);
                    SoundUtil.playSound(player, SoundEvents.ENCHANTMENT_TABLE_USE);
                } else {
                    ItemStack cardHolder = LaserNode.findFirstCardHolder(player);
                    InventoryCardCounts neededItems = getCopiedNodeContents(cardCloner);
                    InventoryCardCounts existingItems = laserNodeBE.getNodeContents();
                    existingItems.subtractInventoryCardCounts(neededItems);
                    boolean enoughItems = true;
                    if (existingItems.hasNegativeValues()) {
                        if (!cardHolder.isEmpty()) {
                            InventoryCardCounts totalExistingItems = existingItems.clone();
                            InventoryCardCounts cardHolderItems = CardHolder.getContents(cardHolder);
                            totalExistingItems.addInventoryCardCounts(cardHolderItems);
                            enoughItems = !totalExistingItems.hasNegativeValues();
                        } else {
                            enoughItems = false;
                        }
                    }
                    if (enoughItems) {
                        transferItems(existingItems.getCardCounts(), existingItems, cardHolder, player);
                        transferItems(existingItems.getCardModifierCounts(), existingItems, cardHolder, player);
                        laserNodeBE.load(nodeTag.copy());
                        laserNodeBE.updateThisNode();
                        player.displayClientMessage(Component.translatable("message.laserio.card_cloner.node_contents_pasted"), true);
                        SoundUtil.playSound(player, SoundEvents.ENCHANTMENT_TABLE_USE);
                    } else {
                        player.displayClientMessage(Component.translatable("message.laserio.card_cloner.insufficient_materials"), true);
                        SoundUtil.playSound(player, SoundEvents.WAXED_SIGN_INTERACT_FAIL);
                        return InteractionResultHolder.pass(cardCloner);
                    }
                }
            } else {
                player.displayClientMessage(Component.translatable("message.laserio.card_cloner.copy_node_first"), true);
                SoundUtil.playSound(player, SoundEvents.WAXED_SIGN_INTERACT_FAIL);
                return InteractionResultHolder.pass(cardCloner);
            }
        }
        return InteractionResultHolder.success(cardCloner);
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
        boolean ctrlPressed = Screen.hasControlDown();

        if (!sneakPressed) {
            tooltip.add(tooltipMaker("laserio.tooltip.item.show_details", ChatFormatting.GRAY));
        } else {
            tooltip.add(tooltipMaker("laserio.tooltip.item.card_cloner.in_node_ui", ChatFormatting.GRAY));
            MutableComponent toWrite = tooltipMaker("laserio.tooltip.item.card_cloner.in_node_ui.copy_card", " - ", ChatFormatting.GRAY);
            toWrite.append(tooltipMaker("laserio.tooltip.item.keys.left_click", ChatFormatting.WHITE));
            tooltip.add(toWrite);
            toWrite = tooltipMaker("laserio.tooltip.item.card_cloner.in_node_ui.paste_card", " - ", ChatFormatting.GRAY);
            toWrite.append(tooltipMaker("laserio.tooltip.item.keys.right_click", ChatFormatting.WHITE));
            tooltip.add(toWrite);

            tooltip.add(tooltipMaker("laserio.tooltip.item.card_cloner.in_world", ChatFormatting.GRAY));
            toWrite = tooltipMaker("laserio.tooltip.item.card_cloner.in_world.copy_node", " - ", ChatFormatting.GRAY);
            toWrite.append(tooltipMaker("laserio.tooltip.item.keys.shift_right_click", ChatFormatting.WHITE));
            tooltip.add(toWrite);
            toWrite = tooltipMaker("laserio.tooltip.item.card_cloner.in_world.paste_node", " - ", ChatFormatting.GRAY);
            toWrite.append(tooltipMaker("laserio.tooltip.item.keys.right_click", ChatFormatting.WHITE));
            tooltip.add(toWrite);
            toWrite = tooltipMaker("laserio.tooltip.item.card_cloner.in_world.clear", " - ", ChatFormatting.GRAY);
            toWrite.append(tooltipMaker("laserio.tooltip.item.keys.shift_right_click", ChatFormatting.WHITE));
            tooltip.add(toWrite);
            toWrite = tooltipMaker("laserio.tooltip.item.card_cloner.in_world.change_paste_mode", " - ", ChatFormatting.GRAY);
            toWrite.append(tooltipMaker("laserio.tooltip.item.keys.right_click", ChatFormatting.WHITE));
            tooltip.add(toWrite);
        }

        if (!ctrlPressed) {
            tooltip.add(tooltipMaker("laserio.tooltip.item.show_settings.ctrl_key", ChatFormatting.GRAY));
        } else {
            appendHoverTextCopiedCard(stack, tooltip);
            appendHoverTextCopiedNode(stack, tooltip);
        }
    }

    @SuppressWarnings("deprecation")
    private static void transferItems(Object2IntOpenHashMap<Item> itemCounts, InventoryCardCounts inventoryCardCounts, ItemStack cardHolder, Player player) {
        itemCounts.object2IntEntrySet().fastForEach(entry -> {
            Item item = entry.getKey();
            int quantity = entry.getIntValue();
            while (quantity != 0) {
                if (quantity < 0) {
                    int quantityToRetrieve = Math.min(-quantity, 64);
                    ItemStack stackToRetrieve = new ItemStack(item, quantityToRetrieve);
                    ItemStack retrievedStack = CardHolder.getCardFromInventory(cardHolder, stackToRetrieve);
                    int retrievedQuantity = retrievedStack.getCount();
                    if (retrievedQuantity != 0) {
                        inventoryCardCounts.addCardModifiersFromCard(retrievedStack);
                        quantity += retrievedQuantity;
                    } else {
                        //This should never happen since we check if we have enough items first,
                        //but just in case we break out of the loop
                        quantity = 0;
                    }
                } else {
                    if (!cardHolder.isEmpty()) {
                        int quantityToDeposit = Math.min(quantity, 64);
                        ItemStack stackToDeposit = new ItemStack(item, quantityToDeposit);
                        ItemStack depositedStack = CardHolder.addCardToInventory(cardHolder, stackToDeposit);
                        int depositedQuantity = quantityToDeposit - depositedStack.getCount();
                        quantity -= depositedQuantity;
                        if (depositedQuantity == quantityToDeposit) {
                            continue;
                        }
                    }
                    int maxStackSize = item.getMaxStackSize();
                    while (quantity != 0) {
                        int quantityToDrop = Math.min(quantity, maxStackSize);
                        ItemStack stackToDrop = new ItemStack(item, quantityToDrop);
                        ItemEntity entityToDrop = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stackToDrop);
                        player.level().addFreshEntity(entityToDrop);
                        quantity -= quantityToDrop;
                    }
                }
            }
        });
    }

    private static void appendHoverTextCopiedCard(ItemStack stack, List<Component> tooltip) {
        tooltip.add(tooltipMaker("laserio.tooltip.item.card_cloner.copied_card", ChatFormatting.GRAY));

        String cardType = getItemType(stack);
        boolean isEnergyCard = false;
        boolean isRedstoneCard = false;
        MutableComponent toWrite = tooltipMaker("laserio.tooltip.item.filter.type", " - ", ChatFormatting.GRAY);
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
        if (cardType.equals("")) {
            toWrite.append(tooltipMaker("laserio.tooltip.item.card.None", cardColor));
        } else {
            toWrite.append(tooltipMaker("item.laserio." + cardType, cardColor));
        }
        tooltip.add(toWrite);
        if (cardType.equals("")) {
            return;
        }

        CompoundTag compoundTag = stack.getOrCreateTag().getCompound("settings");
        int mode = !compoundTag.contains("mode") ? 0 : compoundTag.getByte("mode");
        TransferMode currentMode = TransferMode.values()[mode];
        toWrite = tooltipMaker("laserio.tooltip.item.card.mode", " - ", ChatFormatting.GRAY);
        ChatFormatting modeColor = switch(currentMode) {
            case EXTRACT -> ChatFormatting.RED;
            case INSERT -> ChatFormatting.GREEN;
            case STOCK -> ChatFormatting.BLUE;
            case SENSOR -> ChatFormatting.YELLOW;
            default -> ChatFormatting.GRAY;
        };
        toWrite.append(tooltipMaker("laserio.tooltip.item.card.mode." + currentMode, modeColor));
        tooltip.add(toWrite);

        toWrite = tooltipMaker("laserio.tooltip.item.card.channel", " - ", ChatFormatting.GRAY);
        int channel = !compoundTag.contains("channel") ? 0 : compoundTag.getByte("channel");
        toWrite.append(tooltipMaker(String.valueOf(channel), LaserNodeBERender.COLORS[channel].getRGB()));
        tooltip.add(toWrite);
        if (isRedstoneCard) {
            return;
        }

        if (!isEnergyCard) {
            toWrite = tooltipMaker("laserio.tooltip.item.card.Filter", " - ", ChatFormatting.GRAY);
            ItemStack filterStack = getCopiedCardFilter(stack);
            if (filterStack.isEmpty()) {
                toWrite.append(tooltipMaker("laserio.tooltip.item.card.None", ChatFormatting.WHITE));
            } else {
                toWrite.append(tooltipMaker("item.laserio." + filterStack.getItem(), ChatFormatting.DARK_AQUA));
            }
            tooltip.add(toWrite);
        }

        if (!isEnergyCard || (isEnergyCard && CardEnergyContainer.SLOTS == 1)) {
            ItemStack overclockerStack = getCopiedCardOverclocker(stack);
            if (isEnergyCard) {
                toWrite = tooltipMaker("laserio.tooltip.item.card.Overclocker", " - ", ChatFormatting.GRAY);
                if (overclockerStack.isEmpty()) {
                    toWrite.append(tooltipMaker("laserio.tooltip.item.card.None", ChatFormatting.WHITE));
                } else {
                    toWrite.append(tooltipMaker("item.laserio." + overclockerStack.getItem(), ChatFormatting.DARK_AQUA));
                }
            } else {
                toWrite = tooltipMaker("laserio.tooltip.item.card.Overclockers", " - ", ChatFormatting.GRAY);
                if (overclockerStack.isEmpty()) {
                    toWrite.append(tooltipMaker(String.valueOf(0), ChatFormatting.WHITE));
                } else {
                    toWrite.append(tooltipMaker(String.valueOf(overclockerStack.getCount()), ChatFormatting.DARK_AQUA));
                }
            }
            tooltip.add(toWrite);
        }
    }

    private static void appendHoverTextCopiedNode(ItemStack stack, List<Component> tooltip) {
        tooltip.add(tooltipMaker("laserio.tooltip.item.card_cloner.copied_node", ChatFormatting.GRAY));

        CompoundTag nodeTag = getNodeData(stack);
        MutableComponent toWrite = tooltipMaker("laserio.tooltip.item.card_cloner.copied_node.position", " - ", ChatFormatting.GRAY);
        if (nodeTag.isEmpty()) {
            toWrite.append(tooltipMaker("laserio.tooltip.item.card.None", ChatFormatting.WHITE));
        } else {
            toWrite.append(tooltipMakerLiteral(nodeTag.getCompound("myWorldPos").toString(), ChatFormatting.AQUA));
        }
        tooltip.add(toWrite);
        if (nodeTag.isEmpty()) {
            return;
        }

        toWrite = tooltipMaker("laserio.tooltip.item.card_cloner.copied_node.dimension", " - ", ChatFormatting.GRAY);
        toWrite.append(tooltipMakerLiteral(nodeTag.getString("dimension"), ChatFormatting.DARK_AQUA));
        tooltip.add(toWrite);
    }

    public static void setItemType(ItemStack stack, String itemType) {
        stack.getOrCreateTag().putString("itemType", itemType);
    }

    public static String getItemType(ItemStack stack) {
        return stack.getOrCreateTag().getString("itemType");
    }

    public static void setSettings(ItemStack stack, CompoundTag tag) {
        stack.getOrCreateTag().put("settings", tag);
    }

    public static CompoundTag getSettings(ItemStack stack) {
        return stack.getOrCreateTag().getCompound("settings");
    }

    public static ItemStack getCopiedCardFilter(ItemStack stack) {
        String cardType = getItemType(stack);
        CompoundTag cardTag = getSettings(stack);
        ItemStack filterStack = ItemStack.EMPTY;
        if (!cardType.equals("card_energy") && !cardType.equals("card_redstone")) {
            ItemStackHandler cardInvHandler = new ItemStackHandler(CardItemContainer.SLOTS);
            cardInvHandler.deserializeNBT(cardTag.getCompound("inv"));
            filterStack = cardInvHandler.getStackInSlot(0);
        }
        return filterStack;
    }

    public static ItemStack getCopiedCardOverclocker(ItemStack stack) {
        String cardType = getItemType(stack);
        CompoundTag cardTag = getSettings(stack);
        ItemStack overclockStack = ItemStack.EMPTY;
        if (cardType.equals("card_energy")) {
            if (CardEnergyContainer.SLOTS == 1) {
                ItemStackHandler cardInvHandler = new ItemStackHandler(CardEnergyContainer.SLOTS);
                cardInvHandler.deserializeNBT(cardTag.getCompound("inv"));
                overclockStack = cardInvHandler.getStackInSlot(0);
            }
        } else if (!cardType.equals("card_redstone")) {
            ItemStackHandler cardInvHandler = new ItemStackHandler(CardItemContainer.SLOTS);
            cardInvHandler.deserializeNBT(cardTag.getCompound("inv"));
            overclockStack = cardInvHandler.getStackInSlot(1);
        }
        return overclockStack;
    }

    public static void setNodeData(ItemStack stack, CompoundTag tag) {
        stack.getOrCreateTag().put("nodeData", tag);
    }

    public static CompoundTag getNodeData(ItemStack stack) {
        return stack.getOrCreateTag().getCompound("nodeData");
    }

    public static InventoryCardCounts getCopiedNodeContents(ItemStack stack) {
        InventoryCardCounts nodeContents = new InventoryCardCounts();
        CompoundTag nodeTag = getNodeData(stack);
        if (nodeTag.isEmpty()) {
            return nodeContents;
        }
        for (int i = 0; i < Direction.values().length; i++) {
            ItemStackHandler nodeFaceInvHandler = new ItemStackHandler(LaserNodeContainer.SLOTS);
            nodeFaceInvHandler.deserializeNBT(nodeTag.getCompound("Inventory" + i));
            nodeContents.addHandler(nodeFaceInvHandler);
        }
        return nodeContents;
    }

    public static boolean setPasteNetworkSettings(ItemStack stack, boolean pasteNetworkSettings) {
        if (!pasteNetworkSettings) {
            stack.removeTagKey("pasteNetworkSettings");
        } else {
            stack.getOrCreateTag().putBoolean("pasteNetworkSettings", pasteNetworkSettings);
        }
        return pasteNetworkSettings;
    }

    public static boolean getPasteNetworkSettings(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if (compound == null || !compound.contains("pasteNetworkSettings")) {
            return false;
        }
        return compound.getBoolean("pasteNetworkSettings");
    }
}