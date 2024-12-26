package com.direwolf20.laserio.mixins;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.common.items.upgrades.OverclockerCard;
import com.direwolf20.laserio.setup.Registration;

import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

//Taken with permission from Create-Powerlines
@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    @ModifyVariable(method = "loadFrom", require = 1, at = @At(value = "STORE", ordinal = 0))
    private static Map<String, String> laserio$load(Map<String, String> table) {
        String prefix = "item." + LaserIO.MODID + ".";
        for (RegistryObject<Item> energyOverclocker : Registration.Energy_Overclocker_Cards) {
            int energyTier = ((OverclockerCard) energyOverclocker.get()).getEnergyTier();
            table.put(prefix + energyOverclocker.getId().getPath(), "Energy Overclocker Tier " + energyTier);
        }
        return table;
    }
}