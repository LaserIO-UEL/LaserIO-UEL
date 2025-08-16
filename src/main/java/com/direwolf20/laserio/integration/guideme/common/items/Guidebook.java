package com.direwolf20.laserio.integration.guideme.common.items;

import com.direwolf20.laserio.integration.guideme.GuideMEIntegration;
import com.direwolf20.laserio.util.MiscTools;
import guideme.GuidesCommon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import java.util.List;

public class Guidebook extends Item {
    public Guidebook() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack guidebook = player.getItemInHand(hand);
        if (level.isClientSide()) {
            GuidesCommon.openGuide(player, GuideMEIntegration.GUIDE_ID);
        }
        return InteractionResultHolder.fail(guidebook);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(MiscTools.tooltipMaker("laserio.tooltip.item.guidebook.description", ChatFormatting.GRAY));
    }
}