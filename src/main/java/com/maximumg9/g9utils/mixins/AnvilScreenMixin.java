package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.Util;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @WrapOperation(method = "onSlotUpdate",at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;getString()Ljava/lang/String;"))
    public String onSlotUpdate(Text instance, Operation<String> original) {
        if(G9utils.opt().cheats.opt().dontBeEvilAndWrong) {
            return Util.getSafeString(instance);
        }
        return original.call(instance);
    }
}
