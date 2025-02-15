package com.direwolf20.laserio.mixins;

import com.direwolf20.laserio.setup.Config;
import com.direwolf20.laserio.setup.Registration;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

//Taken with permission from Create-Powerlines
@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    @ModifyVariable(method = "loadFrom", require = 1, at = @At(value = "STORE", ordinal = 0))
    private static Map<String, String> laserio$loadEnergyOverclockersNames(Map<String, String> table) {
        for (int i = 0; i < Registration.Energy_Overclocker_Cards.size(); i++) {
            String name = (i < Config.NAME_TIERS.get().size()) ? Config.NAME_TIERS.get().get(i) : ("Energy Overclocker Tier " + (i + 1));
            table.put(Registration.Energy_Overclocker_Cards.get(i).get().getDescriptionId(), name);
        }
        return table;
    }
}