package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerMixin extends AbstractClientPlayerEntity {
    @Shadow public abstract void move(MovementType movementType, Vec3d movement);

    @Shadow protected abstract void sendMovementPackets();

    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Shadow public Input input;

    public ClientPlayerMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Unique
    private boolean forceSwimming = false;

    @Inject(method="tick", at=@At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(forceSwimming) {
            forceSwimming = false;
            this.setPose(EntityPose.SWIMMING);
        }
    }

    @Inject(method="sendMovementPackets", at=@At("HEAD"), cancellable = true)
    public void sendMovementPackets(CallbackInfo ci) {
        if(forceSwimming) {
            ci.cancel();
        }
    }

    @Redirect(method="tickMovement",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSprinting()Z"))
    public boolean isSprinting(ClientPlayerEntity instance) {
        if(instance.isSprinting() && !this.input.hasForwardMovement()) {
            instance.setSprinting(false);
        }

        return instance.isSprinting() && !G9utils.getOptions().dontStopSprinting;
    }

    @Unique
    @Override
    public void travel(Vec3d movementInput) {
        Vec3d movement = inputToVelocity(movementInput,this.getMovementSpeed(),this.getYaw());
        if(!this.getWorld()
            .isSpaceEmpty(
            this,
                this.getBoundingBox(this.getPose()).offset(this.getPos().add(movement))
            ) && this.getWorld()
                .isSpaceEmpty(
                        this,
                        this.getBoundingBox(EntityPose.SWIMMING)
                                .offset(this.getPos().add(movement))
                )
            && G9utils.getOptions().autoCrawl
        ) {
            this.setPose(EntityPose.STANDING);
            this.move(MovementType.PLAYER,movement);
            this.setPose(EntityPose.SWIMMING);

            forceSwimming = true;
            Vec3d smallMove = new Vec3d(Math.signum(movement.x),Math.signum(movement.y),Math.signum(movement.z));
            smallMove = smallMove.multiply(1E-6);

            Vec3d start = this.getPos();
            Vec3d fin = start.add(smallMove);
            this.setPos(fin.x,fin.y,fin.z);

            this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(this.getX(), this.getY(), this.getZ(), this.isOnGround()));
        } else {
            super.travel(movementInput);
        }
    }

    @Unique
    public Vec3d inputToVelocity(Vec3d movementInput, float speed, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        } else {
            Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
            float f = MathHelper.sin(yaw * 0.017453292F);
            float g = MathHelper.cos(yaw * 0.017453292F);

            return new Vec3d(vec3d.x * (double)g - vec3d.z * (double)f, vec3d.y, vec3d.z * (double)g + vec3d.x * (double)f);
        }
    }
}
