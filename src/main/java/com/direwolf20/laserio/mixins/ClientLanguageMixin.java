package com.direwolf20.laserio.mixins;

import com.direwolf20.laserio.util.MixinUtil;

import net.minecraft.client.resources.language.ClientLanguage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

//Taken with permission from Create-Powerlines
@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    @ModifyVariable(method = "loadFrom", require = 1, at = @At(value = "STORE", ordinal = 0))
    private static Map<String, String> laserio$load(Map<String, String> table) {
        MixinUtil.fillLangTable(table);
        return table;
    }
}