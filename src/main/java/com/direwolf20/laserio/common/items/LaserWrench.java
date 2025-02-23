package com.direwolf20.laserio.common.items;

import com.direwolf20.laserio.common.blockentities.LaserConnectorAdvBE;
import com.direwolf20.laserio.common.blockentities.basebe.BaseLaserBE;
import com.direwolf20.laserio.common.blocks.baseblocks.BaseLaserBlock;
import com.direwolf20.laserio.setup.Config;
import com.direwolf20.laserio.util.DimBlockPos;
import com.direwolf20.laserio.util.VectorHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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

import javax.annotation.Nullable;

import java.util.List;

import static com.direwolf20.laserio.util.MiscTools.tooltipMaker;

public class LaserWrench extends Item {
    public static final BlockPos NULL_CONNECTION_POS = new BlockPos(0, -1000, 0);

    public LaserWrench() {
        super(new Item.Properties()
                .stacksTo(1));
    }

    public static DimBlockPos storeConnectionPos(ItemStack wrench, Level level, BlockPos pos) {
        DimBlockPos dimBlockPos = new DimBlockPos(level, pos);
        wrench.getOrCreateTag().put("connectiondimpos", dimBlockPos.toNBT());
        return dimBlockPos;
    }

    public static DimBlockPos getConnectionPos(ItemStack wrench, Level level) {
        CompoundTag compound = wrench.getOrCreateTag();
        if (level == null) {
            return null;
        }
        return !compound.contains("connectiondimpos") ? storeConnectionPos(wrench, level, NULL_CONNECTION_POS) : new DimBlockPos(compound.getCompound("connectiondimpos"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level targetDim, Player player, InteractionHand hand) {
        ItemStack wrench = player.getItemInHand(hand);
        if (targetDim.isClientSide()) { //No client
            return InteractionResultHolder.success(wrench);
        }
        BlockHitResult lookingAt = VectorHelper.getLookingAt(player, ClipContext.Fluid.NONE, Config.MAX_INTERACTION_RANGE.get());
        if (lookingAt == null || !(targetDim.getBlockState(lookingAt.getBlockPos()).getBlock() instanceof BaseLaserBlock)) {
            if (player.isShiftKeyDown()) {
                storeConnectionPos(wrench, targetDim, NULL_CONNECTION_POS);
            }
            return InteractionResultHolder.pass(wrench);
        }
        BlockPos targetPos = lookingAt.getBlockPos();
        BlockEntity targetBE = targetDim.getBlockEntity(targetPos);
        if (!(targetBE instanceof BaseLaserBE)) {
            return InteractionResultHolder.pass(wrench);
        }
        DimBlockPos sourceDimPos = getConnectionPos(wrench, targetDim);
        Level sourceDim = sourceDimPos.getLevel(targetDim.getServer());
        BlockPos sourcePos = sourceDimPos.blockPos;
        //Shift-click for selecting the first node, click for linking another node to the selected one
        if (player.isShiftKeyDown()) {
            if (targetPos.equals(sourcePos) && targetDim.equals(sourceDim)) {
                //If the wrench's position equals this one, erase it
                storeConnectionPos(wrench, targetDim, NULL_CONNECTION_POS);
            } else {
                //Otherwise, store this position
                storeConnectionPos(wrench, targetDim, targetPos);
            }
            return InteractionResultHolder.pass(wrench);
        } else {
            //If the wrench's position equals this one, return (we don't want to link a node to itself)
            if (targetPos.equals(sourcePos) && targetDim.equals(sourceDim)) {
                return InteractionResultHolder.pass(wrench);
            }
            BlockEntity sourceBE = sourceDim.getBlockEntity(sourcePos);
            //If the sourceBE is not one of ours, erase it
            if (!(sourceBE instanceof BaseLaserBE)) {
                storeConnectionPos(wrench, targetDim, NULL_CONNECTION_POS);
                return InteractionResultHolder.pass(wrench);
            }
            //If both nodes are Advanced, we can connect them despite distance, so skip that check and connect now
            if (targetBE instanceof LaserConnectorAdvBE targetAdv && sourceBE instanceof LaserConnectorAdvBE sourceAdv) {
                targetAdv.handleAdvancedConnection(sourceAdv);
                return InteractionResultHolder.success(wrench);
            }
            //If we're too far away, send an error to the client
            if (!targetPos.closerThan(sourcePos, Config.MAX_NODES_DISTANCE.get()) || !targetDim.equals(sourceDim)) {
                player.displayClientMessage(Component.translatable("message.laserio.wrenchrange", Config.MAX_NODES_DISTANCE.get()), true);
                return InteractionResultHolder.pass(wrench);
            }
            //Connect or disconnect the nodes, depending on current state
            ((BaseLaserBE) targetBE).handleConnection((BaseLaserBE) sourceBE);
        }
        return InteractionResultHolder.success(wrench);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(tooltipMaker("laserio.tooltip.item.show_details", ChatFormatting.GRAY));
        } else {
            MutableComponent toWrite = tooltipMaker("laserio.tooltip.item.laser_wrench.select_node", ChatFormatting.GRAY);
            toWrite.append(tooltipMaker("laserio.tooltip.item.laser_wrench.select_node.keys", ChatFormatting.WHITE));
            tooltip.add(toWrite);
            toWrite = tooltipMaker("laserio.tooltip.item.laser_wrench.link_node", ChatFormatting.GRAY);
            toWrite.append(tooltipMaker("laserio.tooltip.item.laser_wrench.link_node.keys", ChatFormatting.WHITE));
            tooltip.add(toWrite);
            toWrite = tooltipMaker("laserio.tooltip.item.laser_wrench.autolink_node", ChatFormatting.GRAY);
            toWrite.append(tooltipMaker("laserio.tooltip.item.laser_wrench.autolink_node.keys", ChatFormatting.WHITE));
            tooltip.add(toWrite);
        }
    }
}