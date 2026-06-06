package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.maximumg9.g9utils.config.Keybind;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;onKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;)V"))
    public void onButtonPressed(long window, MouseInput input, int action, CallbackInfo ci) {
        Keybind.onMousePressed(input);
    }

    @WrapOperation(method = "onMouseButton",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V"))
    public void setMousePressed(InputUtil.Key key, boolean pressed, Operation<Void> original, @Local(argsOnly = true) MouseInput input) {
        original.call(key,pressed);
        Keybind.setMousePressed(input,pressed);
    }
}
