package com.maximumg9.g9utils.mixins;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements com.maximumg9.g9utils.GameRendererMixinDuck {
    @Unique
    private @Nullable Float fovOverride;

    @Unique
    @Override
    public void g9Utils$setFOVOverride(@Nullable Float fovOverride) {
        this.fovOverride = fovOverride;
    }

    @Inject(method = "getFov",at=@At("HEAD"),cancellable = true)
    public void getFOV(Camera camera, float tickProgress, boolean changingFov, CallbackInfoReturnable<Float> cir) {
        if(fovOverride == null) return;
        cir.setReturnValue(fovOverride);
        cir.cancel();
    }
}
