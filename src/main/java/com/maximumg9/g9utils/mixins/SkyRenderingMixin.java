package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SkyRendering.class)
public class SkyRenderingMixin {
    @Redirect(
        method = "renderSun",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4fStack;mul(Lorg/joml/Matrix4fc;)Lorg/joml/Matrix4f;"
        )
    )
    public Matrix4f changeSkyRenderAngle(Matrix4fStack matrices, Matrix4fc matrix4fc) {
        if(!G9utils.opt().rendering.opt().overrideSkyAngle) return matrices.mul(matrix4fc);
        matrices.popMatrix();
        matrices.pushMatrix();
        matrices.rotate(
            RotationAxis.NEGATIVE_Y.rotationDegrees(
                G9utils.opt().rendering.opt().skyAngleY + 180
            )
        );
        matrices.rotate(
            RotationAxis.NEGATIVE_X.rotationDegrees(
                G9utils.opt().rendering.opt().skyAngleX + 90
            )
        );
        return matrices;
    }

    @ModifyArgs(
        method = "renderSun",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4fStack;scale(FFF)Lorg/joml/Matrix4f;",
            ordinal = 0
        )
    )
    public void changeSunScale(Args args) {
        for(int i=0;i<3;i++) {
            args.set(i, (Float) args.get(i) * G9utils.opt().rendering.opt().sunScale);
        }
    }
}
