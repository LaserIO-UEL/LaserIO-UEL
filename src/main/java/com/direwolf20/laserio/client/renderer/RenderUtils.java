package com.direwolf20.laserio.client.renderer;

import com.direwolf20.laserio.client.events.ClientEvents;
import com.direwolf20.laserio.common.blockentities.LaserConnectorAdvBE;
import com.direwolf20.laserio.common.blockentities.LaserNodeBE;
import com.direwolf20.laserio.common.blockentities.basebe.BaseLaserBE;
import com.direwolf20.laserio.setup.Registration;
import com.direwolf20.laserio.util.CardRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.Color;
import java.util.Queue;
import java.util.Set;

public class RenderUtils {
    public static void render(Matrix4f matrix, VertexConsumer builder, BlockPos pos, Color color, float scale) {
        float red = color.getRed() / 255f, green = color.getGreen() / 255f, blue = color.getBlue() / 255f, alpha = .5f;

        float startX = 0 + (1 - scale) / 2, startY = 0 + (1 - scale) / 2, startZ = -1 + (1 - scale) / 2, endX = 1 - (1 - scale) / 2, endY = 1 - (1 - scale) / 2, endZ = 0 - (1 - scale) / 2;

        //down
        builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();

        //up
        builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();

        //east
        builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();

        //west
        builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();

        //south
        builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).endVertex();

        //north
        builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).endVertex();
    }

    public static Vector3f calculateEndAdvConnector(BlockPos startBlock, BlockPos endBlock, Direction facing) {
        float diffX = endBlock.getX() - startBlock.getX();
        float diffY = endBlock.getY() - startBlock.getY();
        float diffZ = endBlock.getZ() - startBlock.getZ();

        switch (facing) {
            case UP -> {
                diffX += 0.5f;
                diffY -= 0.25f;
                diffZ += 0.5f;
            }
            case DOWN -> {
                diffX += 0.5f;
                diffY += 1.25f;
                diffZ += 0.5f;
            }
            case NORTH -> {
                diffX += 0.5f;
                diffY += 0.5f;
                diffZ += 1.25f;
            }
            case SOUTH -> {
                diffX += 0.5f;
                diffY += 0.5f;
                diffZ -= 0.25f;
            }
            case EAST -> {
                diffX -= 0.25f;
                diffY += 0.5f;
                diffZ += 0.5f;
            }
            case WEST -> {
                diffX += 1.25f;
                diffY += 0.5f;
                diffZ += 0.5f;
            }
        }
        return new Vector3f(diffX, diffY, diffZ);
    }

    public static Vector3f adjustBeamToEyes(Vector3f from, Vector3f to, BlockEntity be) {
        //This method takes the player's position into account, and adjusts the beam so that its rendered properly whereever you stand
        Player player = Minecraft.getInstance().player;
        Vector3f P = new Vector3f((float) player.getX() - be.getBlockPos().getX(), (float) player.getEyeY() - be.getBlockPos().getY(), (float) player.getZ() - be.getBlockPos().getZ());

        Vector3f PS = new Vector3f(from);
        PS.sub(P);
        Vector3f SE = new Vector3f(to);
        SE.sub(from);

        Vector3f adjustedVec = new Vector3f(PS);
        adjustedVec.cross(SE);
        adjustedVec.normalize();
        return adjustedVec;
    }

    public static void addVertexToBuilder(VertexConsumer builder, Matrix4f positionMatrix, Vector3f position,  float r, float g, float b, float alpha, float v1, float v2) {
        builder.vertex(positionMatrix, position.x(), position.y(), position.z());
        if (!ClientEvents.IS_OCULUS_LOADED) {
            builder.color(r, g, b, alpha);
        }
        builder.uv(v1, v2)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880);
        if (ClientEvents.IS_OCULUS_LOADED) {
            builder.color(r, g, b, alpha);
        }
        builder.endVertex();
    }

    public static void drawLaser(VertexConsumer builder, Matrix4f positionMatrix, Vector3f from, Vector3f to, float r, float g, float b, float alpha, float thickness, double v1, double v2, BlockEntity be) {
        Vector3f adjustedVec = adjustBeamToEyes(from, to, be);
        adjustedVec.mul(thickness); //Determines how thick the beam is

        Vector3f p1 = new Vector3f(from);
        p1.add(adjustedVec);
        Vector3f p2 = new Vector3f(from);
        p2.sub(adjustedVec);
        Vector3f p3 = new Vector3f(to);
        p3.add(adjustedVec);
        Vector3f p4 = new Vector3f(to);
        p4.sub(adjustedVec);

        addVertexToBuilder(builder, positionMatrix, p1, r, g, b, alpha, 1, (float) v1);
        addVertexToBuilder(builder, positionMatrix, p3, r, g, b, alpha, 1, (float) v2);
        addVertexToBuilder(builder, positionMatrix, p4, r, g, b, alpha, 0, (float) v2);
        addVertexToBuilder(builder, positionMatrix, p2, r, g, b, alpha, 0, (float) v1);
    }

    public static void drawLasers(Queue<BaseLaserBE> beRenders, PoseStack matrixStackIn) {
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder;
        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        builder = buffer.getBuffer(MyRenderType.CONNECTING_LASER);
        while (beRenders.size() > 0) {
            BaseLaserBE be = beRenders.remove();
            Level level = be.getLevel();
            long gameTime = level.getGameTime();
            double v = gameTime * 0.04;
            BlockPos startBlock = be.getBlockPos();
            matrixStackIn.pushPose();
            Matrix4f positionMatrix = matrixStackIn.last().pose();

            matrixStackIn.translate(startBlock.getX() - projectedView.x, startBlock.getY() - projectedView.y, startBlock.getZ() - projectedView.z);

            Vector3f startLaser = new Vector3f(0.5f, .5f, 0.5f);
            for (BlockPos target : be.getRenderedConnections()) {
                BlockPos endBlock = be.getWorldPos(target);
                Color color = be.getColor();
                Player player = Minecraft.getInstance().player;
                ItemStack wrench = ClientEvents.getWrench(player);
                int alpha = wrench.isEmpty() ? color.getAlpha() : Math.min(color.getAlpha() + be.getWrenchAlpha(), 255);
                float diffX = endBlock.getX() + .5f - startBlock.getX();
                float diffY = endBlock.getY() + .5f - startBlock.getY();
                float diffZ = endBlock.getZ() + .5f - startBlock.getZ();
                Vector3f endLaser = new Vector3f(diffX, diffY, diffZ);
                drawLaser(builder, positionMatrix, endLaser, startLaser, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha / 255f, 0.025f, v, v + diffY * 1.5, be);
            }

            if (be instanceof LaserConnectorAdvBE laserConnectorAdvBE && laserConnectorAdvBE.getPartnerDimBlockPos() != null && level.getBlockState(be.getBlockPos()).getBlock().equals(Registration.LaserConnectorAdv.get())) {
                Direction facing = level.getBlockState(be.getBlockPos()).getValue(BlockStateProperties.FACING).getOpposite();
                BlockPos endBlock = laserConnectorAdvBE.getBlockPos().relative(facing);
                Color color = be.getColor();
                Player player = Minecraft.getInstance().player;
                ItemStack wrench = ClientEvents.getWrench(player);
                int alpha = wrench.isEmpty() ? color.getAlpha() : Math.min(color.getAlpha() + be.getWrenchAlpha(), 255);
                Vector3f endLaser = calculateEndAdvConnector(startBlock, endBlock, facing);
                drawLaser(builder, positionMatrix, endLaser, startLaser, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha / 255f, 0.025f, v, v + endLaser.y() * 1.5, be);
            }
            matrixStackIn.popPose();
        }
        buffer.endBatch(MyRenderType.CONNECTING_LASER); //This apparently is needed in RenderWorldLast
    }

    public static void drawConnectingLasersMainBeam(Set<LaserNodeBE> beConnectingRenders, PoseStack matrixStackIn, MultiBufferSource.BufferSource buffer, Vec3 projectedView, float alpha, float thickness) {
        VertexConsumer builder = buffer.getBuffer(MyRenderType.LASER_MAIN_BEAM);
        for (LaserNodeBE be : beConnectingRenders) {
            Level level = be.getLevel();
            long gameTime = level.getGameTime();
            double v = gameTime * 0.04;

            BlockPos startBlock = be.getBlockPos();

            matrixStackIn.pushPose();
            Matrix4f positionMatrix = matrixStackIn.last().pose();
            matrixStackIn.translate(startBlock.getX() - projectedView.x, startBlock.getY() - projectedView.y, startBlock.getZ() - projectedView.z);

            for (CardRender cardRender : be.cardRenders) {
                drawLaser(builder, positionMatrix, cardRender.endLaser, cardRender.startLaser, cardRender.r, cardRender.g, cardRender.b, alpha, thickness, v, v + cardRender.diffY * 4.5, be);
            }
            matrixStackIn.popPose();
        }
        buffer.endBatch(MyRenderType.LASER_MAIN_BEAM); //This apparently is needed in RenderWorldLast
    }

    public static void drawConnectingLasersMainCore(Set<LaserNodeBE> beConnectingRenders, PoseStack matrixStackIn, MultiBufferSource.BufferSource buffer, Vec3 projectedView, float alpha, float thickness) {
        VertexConsumer builder = buffer.getBuffer(MyRenderType.LASER_MAIN_CORE);
        for (LaserNodeBE be : beConnectingRenders) {
            Level level = be.getLevel();
            long gameTime = level.getGameTime();
            double v = gameTime * 0.04;

            BlockPos startBlock = be.getBlockPos();

            matrixStackIn.pushPose();
            Matrix4f positionMatrix = matrixStackIn.last().pose();
            matrixStackIn.translate(startBlock.getX() - projectedView.x, startBlock.getY() - projectedView.y, startBlock.getZ() - projectedView.z);

            for (CardRender cardRender : be.cardRenders) {
                drawLaser(builder, positionMatrix, cardRender.endLaser, cardRender.startLaser, cardRender.floatColors[0], cardRender.floatColors[1], cardRender.floatColors[2], 1f, 0.0125f, v, v + cardRender.diffY * 1.5, be);
            }
            matrixStackIn.popPose();
        }
        buffer.endBatch(MyRenderType.LASER_MAIN_CORE); //This apparently is needed in RenderWorldLast
    }

    public static void drawConnectingLasers(Set<LaserNodeBE> beConnectingRenders, PoseStack matrixStackIn) {
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        float alpha = 1f;
        float thickness = 0.0175f;

        if (ClientEvents.IS_OCULUS_LOADED) {
            drawConnectingLasersMainCore(beConnectingRenders, matrixStackIn, buffer, projectedView, alpha, thickness);
            drawConnectingLasersMainBeam(beConnectingRenders, matrixStackIn, buffer, projectedView, alpha, thickness);
        } else {
            drawConnectingLasersMainBeam(beConnectingRenders, matrixStackIn, buffer, projectedView, alpha, thickness);
            drawConnectingLasersMainCore(beConnectingRenders, matrixStackIn, buffer, projectedView, alpha, thickness);
        }
    }
}