package com.direwolf20.laserio.common.blocks.baseblocks;

import com.direwolf20.laserio.common.blockentities.LaserConnectorAdvBE;
import com.direwolf20.laserio.common.blockentities.basebe.BaseLaserBE;
import com.direwolf20.laserio.common.items.LaserWrench;
import com.direwolf20.laserio.setup.Config;
import com.direwolf20.laserio.util.DimBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BaseLaserBlock extends Block {
    public BaseLaserBlock() {
        super(Properties.of()
                .sound(SoundType.METAL)
                .strength(2.0f)
                .noOcclusion()
                .forceSolidOn()
        );
    }

    @Override
    public void setPlacedBy(Level targetDim, BlockPos targetPos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(targetDim, targetPos, state, entity, stack);
        //We check if the entity's class matches ServerPlayer because instanceof returns
        //true for subclasses as well and we want to avoid executing the following code
        //if the node is placed by fake players.
        //Like that we also cover some edge cases where fake players aren't implemented
        //by extending the Forge FakePlayer class
        if (!entity.getClass().equals(ServerPlayer.class)) {
            return;
        }
        Player player = (ServerPlayer) entity;
        ItemStack heldItemOffhand = player.getOffhandItem();
        if (!(heldItemOffhand.getItem() instanceof LaserWrench)) {
            return;
        }
        BlockEntity targetBE = targetDim.getBlockEntity(targetPos);
        DimBlockPos sourceDimPos = LaserWrench.getConnectionPos(heldItemOffhand, targetDim);
        Level sourceDim = sourceDimPos.getLevel(targetDim.getServer());
        BlockPos sourcePos = sourceDimPos.blockPos;
        BlockEntity sourceBE = sourceDim.getBlockEntity(sourcePos);
        if (sourceBE instanceof BaseLaserBE) {
            if (targetBE instanceof LaserConnectorAdvBE targetAdv && sourceBE instanceof LaserConnectorAdvBE sourceAdv) {
                //If both nodes are Advanced, we can connect them despite distance, so skip that check and connect now
                targetAdv.handleAdvancedConnection(sourceAdv);
            } else if (!targetPos.closerThan(sourcePos, Config.MAX_NODES_DISTANCE.get()) || !targetDim.equals(sourceDim)) {
                //If we're too far away, send an error to the client
                player.displayClientMessage(Component.translatable("message.laserio.wrenchrange", Config.MAX_NODES_DISTANCE.get()), true);
            } else {
                //Connect the node to the network
                ((BaseLaserBE) targetBE).addConnection(sourceDimPos.blockPos, (BaseLaserBE) sourceBE);
            }
        }
        //Store this position for the next connection
        LaserWrench.storeConnectionPos(heldItemOffhand, targetDim, targetPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() != this) {
            BlockEntity be = worldIn.getBlockEntity(pos);
            if (be instanceof BaseLaserBE baseLaserBE) {
                baseLaserBE.disconnectAllNodes();
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
}