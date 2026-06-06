package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.maximumg9.g9utils.CylinderRenderHelper;
import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.config.Keybind;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Redirect(method = "onKey",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/util/ScreenshotRecorder;saveScreenshot(Ljava/io/File;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V"))
    public void cylindricalSS(File gameDirectory, Framebuffer framebuffer, Consumer<Text> messageReceiver, @Local(argsOnly = true) KeyInput keyInput) {
        if (keyInput.hasShift() && G9utils.opt().rendering.cylindricalScreenshot) {
            CylinderRenderHelper.takeCylindricalScrenshot(gameDirectory,messageReceiver);
        } else {
            ScreenshotRecorder.saveScreenshot(gameDirectory, framebuffer, messageReceiver);
        }
    }

    @WrapOperation(method = "onKey",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V"))
    public void setKeyPressed(InputUtil.Key key, boolean pressed, Operation<Void> original, @Local(argsOnly = true) KeyInput input) {
        original.call(key,pressed);
        Keybind.setKeyPressed(input,pressed);
    }

    @Inject(method = "onKey",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;onKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;)V"))
    public void onKeyPressed(long window, int action, KeyInput input, CallbackInfo ci) {
        Keybind.onKeyPressed(input);
    }
}
