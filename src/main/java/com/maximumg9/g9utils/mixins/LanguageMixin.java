package com.maximumg9.g9utils.mixins;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.sugar.Local;
import com.maximumg9.g9utils.G9utils;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Language.class)
public class LanguageMixin {
    @Inject(method = "create",at = @At(value = "INVOKE",target="Ljava/util/Map;copyOf(Ljava/util/Map;)Ljava/util/Map;"))
    private static void storeVanillaLangData(CallbackInfoReturnable<Language> cir, @Local Map<String, String> map) {
        G9utils.VANILLA_LANGUAGE_KEYS = ImmutableSet.copyOf(map.keySet());
    }
}
