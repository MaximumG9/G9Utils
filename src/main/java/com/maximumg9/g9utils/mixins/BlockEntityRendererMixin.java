package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderer.class)
public interface BlockEntityRendererMixin<T extends BlockEntity> {
    @Inject(method = "isInRenderDistance",at=@At("HEAD"),cancellable = true)
    default void isInRenderDistance(T blockEntity, Vec3d pos, CallbackInfoReturnable<Boolean> cir) {
        if(G9utils.opt().rendering.opt().alwaysRenderBlockEntities) {
            cir.cancel();
            cir.setReturnValue(true);
        }
    }
}
