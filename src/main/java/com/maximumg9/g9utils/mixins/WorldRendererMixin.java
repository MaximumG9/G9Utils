package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Redirect(
        method = "renderSky",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/MatrixStack;peek()Lnet/minecraft/client/util/math/MatrixStack$Entry;",
            ordinal = 2
        )
    )
    public MatrixStack.Entry changeSkyRenderAngle(MatrixStack instance) {
        if(!G9utils.opt().rendering.opt().overrideSkyAngle) return instance.peek();
        instance.pop();
        instance.push();
        instance.multiply(
            RotationAxis.NEGATIVE_Y.rotationDegrees(
                G9utils.opt().rendering.opt().skyAngleY + 180
            )
        );
        instance.multiply(
            RotationAxis.NEGATIVE_X.rotationDegrees(
                G9utils.opt().rendering.opt().skyAngleX + 90
            )
        );
        return instance.peek();
    }

    @ModifyVariable(
        method = "renderSky",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V",
            ordinal = 1
        ),
        ordinal = 5
    )
    public float changeSunScale(float value) {
        return value * G9utils.opt().rendering.opt().sunScale;
    }
}
