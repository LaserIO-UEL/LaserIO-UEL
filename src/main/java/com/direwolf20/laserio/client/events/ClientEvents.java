package com.direwolf20.laserio.client.events;

import com.direwolf20.laserio.client.renderer.BlockOverlayRender;
import com.direwolf20.laserio.client.renderer.DelayedRenderer;
import com.direwolf20.laserio.common.blockentities.LaserConnectorAdvBE;
import com.direwolf20.laserio.common.blockentities.basebe.BaseLaserBE;
import com.direwolf20.laserio.common.blocks.LaserConnectorAdv;
import com.direwolf20.laserio.common.items.LaserWrench;
import com.direwolf20.laserio.setup.Config;
import com.direwolf20.laserio.util.DimBlockPos;
import com.direwolf20.laserio.util.VectorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {
    @SubscribeEvent
    static void renderWorldLastEvent(RenderLevelStageEvent evt) {
        if (evt.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        ItemStack wrench = getWrench(player);
        if (!wrench.isEmpty()) {
            Level level = player.level();
            DimBlockPos selectedDimPos = LaserWrench.getConnectionPos(wrench, level);
            if (selectedDimPos != null && level.dimension().equals(selectedDimPos.levelKey)) {
                BlockPos selectedPos = selectedDimPos.blockPos;
                BlockEntity selectedBE = level.getBlockEntity(selectedPos);
                if (selectedBE instanceof BaseLaserBE baseLaserBE) {
                    BlockOverlayRender.renderSelectedBlock(evt, selectedPos, baseLaserBE);
                }
            }
        }
        //DelayedRenderer Renders
        DelayedRenderer.render(evt.getPoseStack());
        DelayedRenderer.renderConnections(evt.getPoseStack());
    }

    public static ItemStack getWrench(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof LaserWrench)) {
            heldItem = player.getOffhandItem();
            if (!(heldItem.getItem() instanceof LaserWrench)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }

    @SubscribeEvent
    static void renderGUIOverlay(CustomizeGuiOverlayEvent.DebugText evt) {
        Player player = Minecraft.getInstance().player;
        if (getWrench(player).isEmpty()) {
            return;
        }
        BlockHitResult lookingAt = VectorHelper.getLookingAt(player, ClipContext.Fluid.NONE, Config.MAX_INTERACTION_RANGE.get());
        if (lookingAt == null) {
            return;
        }
        Level level = player.level();
        BlockPos blockPos = lookingAt.getBlockPos();
        if (!(level.getBlockState(blockPos).getBlock() instanceof LaserConnectorAdv)) {
            return;
        }
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof LaserConnectorAdvBE laserConnectorAdvBE) {
            GuiGraphics guiGraphics = evt.getGuiGraphics();
            Font font = Minecraft.getInstance().font;
            RenderGUIOverlay.renderLocation(font, guiGraphics, laserConnectorAdvBE);
        }
    }
}