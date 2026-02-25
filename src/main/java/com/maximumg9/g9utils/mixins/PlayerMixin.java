package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.maximumg9.g9utils.ClientCommonNetworkHandlerMixinDuck;
import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.SwordHitType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements com.maximumg9.g9utils.PlayerMixinDuck {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    @Final
    private PlayerAbilities abilities;

    @Inject(method = "tryDeflect",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;)V", ordinal = 0))
    public void redirect(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        G9utils.lastSwordHitType = SwordHitType.REDIRECT;
    }

    @Inject(method = "attack",at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;sidedDamage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    public void attack(Entity target, CallbackInfo ci, @Local(ordinal = 2) float h) {
        if(this.getEntityWorld().isClient()) {

            @SuppressWarnings("DataFlowIssue")
            ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

            boolean isServerSideSprinting = ((ClientCommonNetworkHandlerMixinDuck)
                player.networkHandler).g9Utils$isServerSideSprinting();

            if(isServerSideSprinting && h > 0.9f) {
                G9utils.lastSwordHitType = SwordHitType.SPRINT;
            } else if(h > 0.9f && this.fallDistance > 0.0F && !this.isOnGround() && !this.isClimbing() && !this.isTouchingWater() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && !this.hasVehicle() && target instanceof LivingEntity) {
                G9utils.lastSwordHitType = SwordHitType.CRIT;
            } else if(
                h > 0.9f && this.isOnGround() &&
                this.getMovement().horizontalLengthSquared() <
                    MathHelper.square((double)this.getMovementSpeed() * 2.5)
            ) {
                G9utils.lastSwordHitType = SwordHitType.SWEEP;
            } else {
                G9utils.lastSwordHitType = SwordHitType.NORMAL;
            }

            ((ClientCommonNetworkHandlerMixinDuck)
                player.networkHandler).g9Utils$setServerSideSprinting(false);
        }
    }

    @Override
    public void jump() {
        float f = this.getJumpVelocity();
        if (f <= 1.0E-5f) {
            return;
        }
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x, Math.max(f, vec3d.y), vec3d.z);
        if (this.isSprinting() && !G9utils.opt().cheats.opt().quakeAir) {
            float g = this.getYaw() * ((float)Math.PI / 180);
            this.addVelocityInternal(new Vec3d((double)(-MathHelper.sin(g)) * 0.2, 0.0, (double)MathHelper.cos(g) * 0.2));
        }
        this.velocityDirty = true;
    }

    @Unique
    private static final double SEC_PER_TICK = 1.0/20;

    @Override
    public boolean g9Utils$wasAirborneLastFrame() {
        return this.lastTickAirborne;
    }

    @Unique
    @Override
    public double g9Utils$getLastCurrentSpeed() {
        return this.lastCurrentSpeed;
    }

    @Unique
    private boolean lastTickAirborne = false;

    @Unique
    private double lastCurrentSpeed = 0;

    @Override
    protected void travelMidAir(Vec3d movementInput) {
        if(
            !this.getEntityWorld().isClient() ||
            (this.isOnGround() && !lastTickAirborne) ||
            !G9utils.opt().cheats.opt().quakeAir ||
            (this.abilities.flying)
        ) {
            super.travelMidAir(movementInput);
            lastTickAirborne = !this.isOnGround();
            return;
        }

        this.airAccelerate(movementInput);
        double g = -this.getEffectiveGravity() * 0.8;

        this.addVelocity(0,g,0);
        lastTickAirborne = !this.isOnGround();
        this.move(MovementType.SELF, this.getVelocity());

        if(this.isOnGround() && this.getVelocity().y == 0) {
            this.addVelocity(0,g,0);
        }
    }

    @Unique
    public void airAccelerate(Vec3d movementInput) {
        float wishspeed = this.getMovementSpeed();
        float accel = G9utils.opt().cheats.opt().airAccelerate;
        Vec3d wishDir = getWishDir(movementInput, this.getYaw()).normalize();


        float wishspd = wishspeed;
        double addspeed, accelspeed;

        if(wishspd > 0.20f)
            wishspd = 0.20f;

        double currentspeed = this.getVelocity().dotProduct(wishDir);

        addspeed = wishspd - currentspeed;
        this.lastCurrentSpeed = currentspeed;
        if(addspeed <= 0)
            return;

        accelspeed = accel * wishspeed * SEC_PER_TICK;
        if(accelspeed > addspeed)
            accelspeed = addspeed;


        this.addVelocity(wishDir.multiply(accelspeed));

        // ground decel
        if(this.isOnGround()) {
            BlockPos blockPos = this.getVelocityAffectingPos();
            float friction = MathHelper.sqrt(MathHelper.sqrt(this.getEntityWorld().getBlockState(blockPos).getBlock().getSlipperiness()));
            Vec3d oldVelocity = this.getVelocity();
            this.setVelocity(oldVelocity.x * friction, oldVelocity.y * 0.98, oldVelocity.z * friction);
        }

    }

    @Unique
    private static Vec3d getWishDir(Vec3d movementInput, float yaw) {
        float sin = MathHelper.sin(yaw * ((float)Math.PI / 180));
        float cos = MathHelper.cos(yaw * ((float)Math.PI / 180));
        return new Vec3d(
            movementInput.x * (double)cos - movementInput.z * (double)sin,
            movementInput.y,
            movementInput.z * (double)cos + movementInput.x * (double)sin
        );
    }

    @Inject(method = "tickMovement",at=@At("HEAD"))
    public void tickMovement(CallbackInfo ci) {
        if(G9utils.opt().cheats.opt().constantJump)
            this.jumpingCooldown = 0;
    }
    /// Original Quake AirAccelerate
    ///void PM_AirAccelerate (vec3_t wishdir, float wishspeed, float accel)
    ///{
    ///    int			i;
    ///    float		addspeed, accelspeed, currentspeed, wishspd = wishspeed;
    ///
    ///    if (pmove.dead)
    ///        return;
    ///    if (pmove.waterjumptime)
    ///        return;
    ///
    ///    if (wishspd > 30)
    ///        wishspd = 30;
    ///    currentspeed = DotProduct (pmove.velocity, wishdir);
    ///    addspeed = wishspd - currentspeed;
    ///    if (addspeed <= 0)
    ///        return;
    ///    accelspeed = accel * wishspeed * frametime;
    ///    if (accelspeed > addspeed)
    ///        accelspeed = addspeed;
    ///
    ///    for (i=0 ; i<3 ; i++)
    ///        pmove.velocity[i] += accelspeed*wishdir[i];
    ///}
}
