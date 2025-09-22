package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

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
