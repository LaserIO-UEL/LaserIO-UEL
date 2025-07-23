package com.direwolf20.laserio.client.events;

import com.direwolf20.laserio.client.renderer.BlockOverlayRender;
import com.direwolf20.laserio.client.renderer.DelayedRenderer;
import com.direwolf20.laserio.common.blockentities.LaserConnectorAdvBE;
import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.blockentities.basebe.BaseLaserBE;
import com.direwolf20.laserio.common.blocks.LaserConnectorAdv;
import com.direwolf20.laserio.common.items.CardCloner;
import com.direwolf20.laserio.common.items.LaserWrench;
import com.direwolf20.laserio.integration.ModIntegration;
import com.direwolf20.laserio.setup.Config;
import com.direwolf20.laserio.util.DimBlockPos;
import com.direwolf20.laserio.util.VectorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.Color;

public class ClientEvents {
    private static final Stage DEFAULT_RENDERING_STAGE = Stage.AFTER_CUTOUT_BLOCKS;
    private static final Stage OCULUS_RENDERING_STAGE = Stage.AFTER_TRANSLUCENT_BLOCKS;

    @SubscribeEvent
    static void renderWorldLastEvent(RenderLevelStageEvent evt) {
        Stage renderingStage = ModIntegration.OCULUS.isLoaded() ? OCULUS_RENDERING_STAGE : DEFAULT_RENDERING_STAGE;
        if (evt.getStage() != renderingStage) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        Level level = player.level();
        ResourceKey<Level> dimension = level.dimension();
        ItemStack wrench = findWrench(player);
        if (!wrench.isEmpty()) {
            DimBlockPos selectedDimPos = LaserWrench.getConnectionPos(wrench, level);
            if (selectedDimPos != null && dimension.equals(selectedDimPos.levelKey)) {
                BlockPos selectedPos = selectedDimPos.blockPos;
                BlockEntity selectedBE = level.getBlockEntity(selectedPos);
                if (selectedBE instanceof BaseLaserBE baseLaserBE) {
                    BlockOverlayRender.renderSelectedBlock(evt, selectedPos, baseLaserBE, Color.GREEN);
                }
            }
        }
        ItemStack cardCloner = findCardHolder(player);
        if (!cardCloner.isEmpty()) {
            CompoundTag copiedNodeTag = CardCloner.getNodeData(cardCloner);
            if (!copiedNodeTag.isEmpty()) {
                String copiedNodeDim = copiedNodeTag.getString("dimension");
                if (dimension.location().toShortLanguageKey().equals(copiedNodeDim)) {
                    BlockPos copiedNodePos = NbtUtils.readBlockPos(copiedNodeTag.getCompound("myWorldPos"));
                    BlockEntity copiedNodeBE = level.getBlockEntity(copiedNodePos);
                    if (copiedNodeBE instanceof LaserNodeBE laserNodeBE) {
                        BlockOverlayRender.renderSelectedBlock(evt, copiedNodePos, laserNodeBE, Color.CYAN);
                    }
                }
            }
        }
        //DelayedRenderer Renders
        DelayedRenderer.render(evt.getPoseStack());
        DelayedRenderer.renderConnections(evt.getPoseStack());
    }

    public static ItemStack findItemInHands(Player player, Class<? extends Item> itemClass) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(itemClass.isInstance(heldItem.getItem()))) {
            heldItem = player.getOffhandItem();
            if (!(itemClass.isInstance(heldItem.getItem()))) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }

    public static ItemStack findWrench(Player player) {
        return findItemInHands(player, LaserWrench.class);
    }

    public static ItemStack findCardHolder(Player player) {
        return findItemInHands(player, CardCloner.class);
    }

    @SubscribeEvent
    static void renderGUIOverlay(CustomizeGuiOverlayEvent.DebugText evt) {
        Player player = Minecraft.getInstance().player;
        if (findWrench(player).isEmpty()) {
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