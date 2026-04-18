package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.LinearPositionalInterpolator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PositionInterpolator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @WrapOperation(method = "<init>",
        at= @At(
            value = "NEW",
            args = "class=net/minecraft/entity/PositionInterpolator"
        )
    )
    public PositionInterpolator fun(Entity e, Operation<PositionInterpolator> op) {
        return new LinearPositionalInterpolator(e,null);
    }

    @Shadow private boolean noDrag;

    @Inject(method="hasNoDrag",at=@At(value = "HEAD"), cancellable = true)
    public void hasNoDrag(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!G9utils.opt().cheats.opt().deceleration || this.noDrag);
    }

    @Redirect(method="tickMovement",at= @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;sidewaysSpeed:F", opcode = Opcodes.PUTFIELD))
    private void modifySidewaysSpeed(LivingEntity entity, float value) {
        entity.sidewaysSpeed = G9utils.opt().cheats.opt().deceleration ? entity.sidewaysSpeed * 0.98f : entity.sidewaysSpeed;
    }

    @Redirect(method="tickMovement",at= @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;forwardSpeed:F", opcode = Opcodes.PUTFIELD))
    private void modifyForwardsSpeed(LivingEntity entity, float value) {
        entity.forwardSpeed = G9utils.opt().cheats.opt().deceleration ? entity.forwardSpeed * 0.98f : entity.forwardSpeed;
    }
}
