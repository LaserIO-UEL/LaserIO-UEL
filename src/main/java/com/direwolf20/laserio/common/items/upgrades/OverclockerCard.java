package com.direwolf20.laserio.common.items.upgrades;

import com.direwolf20.laserio.setup.Config;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import java.util.List;

public class OverclockerCard extends Item {
    private int energyTier;

    public OverclockerCard(int tier) {
        super(new Item.Properties());

        this.energyTier = tier;
    }

    public int getEnergyTier() {
        return energyTier;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        if (energyTier < 0)
            return;

        tooltip.add(Component.translatable("laserio.tooltip.item.energy_overclocker.max_fe", Config.MAX_FE_TIERS.get().get(energyTier - 1))
                .withStyle(ChatFormatting.GRAY));
    }
}