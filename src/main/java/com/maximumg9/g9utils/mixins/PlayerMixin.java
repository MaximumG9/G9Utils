package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.maximumg9.g9utils.ClientCommonNetworkHandlerMixinDuck;
import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.SwordHitType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;)V", ordinal = 0))
    public void redirect(Entity target, CallbackInfo ci) {
        G9utils.lastSwordHitType = SwordHitType.REDIRECT;
    }

    @Inject(method = "attack",at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    public void attack(Entity target, CallbackInfo ci, @Local(ordinal = 2) float h) {
        if(this.getWorld().isClient) {

            @SuppressWarnings("DataFlowIssue")
            ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

            boolean isServerSideSprinting = ((ClientCommonNetworkHandlerMixinDuck)
                player.networkHandler).g9Utils$isServerSideSprinting();

            if(isServerSideSprinting && h > 0.9f) {
                G9utils.lastSwordHitType = SwordHitType.SPRINT;
            } else if(h > 0.9f && this.fallDistance > 0.0F && !this.isOnGround() && !this.isClimbing() && !this.isTouchingWater() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && !this.hasVehicle() && target instanceof LivingEntity) {
                G9utils.lastSwordHitType = SwordHitType.CRIT;
            } else if(h > 0.9f && this.isOnGround() && this.horizontalSpeed - this.prevHorizontalSpeed < this.getMovementSpeed()) {
                G9utils.lastSwordHitType = SwordHitType.SWEEP;
            } else {
                G9utils.lastSwordHitType = SwordHitType.NORMAL;
            }

            ((ClientCommonNetworkHandlerMixinDuck)
                player.networkHandler).g9Utils$setServerSideSprinting(false);
        }
    }
}
