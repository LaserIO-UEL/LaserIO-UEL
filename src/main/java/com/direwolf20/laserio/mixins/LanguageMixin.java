package com.direwolf20.laserio.mixins;

import com.direwolf20.laserio.util.MixinUtil;

import net.minecraft.locale.Language;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

//Taken with permission from Create-Powerlines
@Mixin(Language.class)
public class LanguageMixin {
    @ModifyArg(method = "loadDefault", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/server/LanguageHook;captureLanguageMap(Ljava/util/Map;)V"))
    private static Map<String, String> laserio$load(Map<String, String> table) {
        MixinUtil.fillLangTable(table);
        return table;
    }
}