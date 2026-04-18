package com.maximumg9.g9utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.PositionInterpolator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class LinearPositionalInterpolator extends PositionInterpolator {
    @Nullable
    public Vec3d lastRecvdPos = null;
    public Vec3d posLastTick;
    @Nullable
    public Vec3d prevRecvdPos = null;

    public float lastRecvdPitch = 0;
    public float pitchLastTick = 0;
    public float prevRecvdPitch = 0;

    public float lastRecvdYaw = 0;
    public float yawLastTick = 0;
    public float prevRecvdYaw = 0;

    private int ticksSinceLastData = 0;

    private boolean active = false;

    public LinearPositionalInterpolator(Entity entity, @Nullable Consumer<PositionInterpolator> callback) {
        super(entity, 3, callback);
    }

    @Override
    public void refreshPositionAndAngles(Vec3d pos, float yaw, float pitch) {
        if(G9utils.opt().cheats.opt().disableMultiTickInterpolation) {
            active = true;
            ticksSinceLastData = 0;
            prevRecvdPos = lastRecvdPos;
            prevRecvdPitch = lastRecvdPitch;
            prevRecvdYaw = lastRecvdYaw;
            this.lastRecvdPos = pos;
            this.lastRecvdYaw = yaw;
            this.lastRecvdPitch = pitch;
        } else {
            super.refreshPositionAndAngles(pos,yaw,pitch);
        }
    }

    @Override
    public Vec3d getLerpedPos() {
        if(G9utils.opt().cheats.opt().disableMultiTickInterpolation) {
            return this.lastRecvdPos;
        } else {
            return super.getLerpedPos();
        }
    }

    @Override
    public float getLerpedYaw() {
        if(G9utils.opt().cheats.opt().disableMultiTickInterpolation) {
            return this.lastRecvdYaw;
        } else {
            return super.getLerpedYaw();
        }
    }

    @Override
    public float getLerpedPitch() {
        if(G9utils.opt().cheats.opt().disableMultiTickInterpolation) {
            return this.lastRecvdPitch;
        } else {
            return super.getLerpedPitch();
        }
    }

    @Override
    public void tick() {
        if(!G9utils.opt().cheats.opt().disableMultiTickInterpolation) {
            super.tick();
            return;
        }
        if(!active) return;

        ticksSinceLastData++;
        if (this.posLastTick != null && lastRecvdPos != null) {
            Vec3d vec3d = this.entity.getEntityPos().subtract(this.posLastTick);
            if (this.entity.getEntityWorld().isSpaceEmpty(
                this.entity,
                this.entity.getDimensions(this.entity.getPose())
                    .getBoxAt(this.lastRecvdPos.add(vec3d))
            )) {
                this.lastRecvdPos = this.lastRecvdPos.add(vec3d);
            }
        }

        float yawDiff = this.entity.getYaw() - this.yawLastTick;
        float pitchDiff = this.entity.getPitch() - this.pitchLastTick;

        this.lastRecvdYaw = lastRecvdYaw + yawDiff;
        this.lastRecvdPitch = lastRecvdPitch + pitchDiff;

        float delta = Math.min((float) ticksSinceLastData,this.lerpDuration) / (this.lerpDuration - 1)
            + G9utils.opt().cheats.opt().interpOffset;

        // figure out why needed
        if(lastRecvdPos != null) {
            if (prevRecvdPos == null) {
                this.entity.setPosition(this.lastRecvdPos);
            } else {
                this.entity.setPosition(
                    Util.interpVec(delta, this.prevRecvdPos, this.lastRecvdPos)
                );
            }
        }

        this.entity.setRotation(
            MathHelper.lerpAngleDegrees(delta,this.prevRecvdYaw,this.lastRecvdYaw),
            MathHelper.lerp(delta,this.prevRecvdPitch,this.lastRecvdPitch)
        );

        this.yawLastTick = this.entity.getYaw();
        this.pitchLastTick = this.entity.getPitch();
        this.posLastTick = this.entity.getEntityPos();
    }

    @Override
    public void clear() {
        super.clear();
        // Maybe should do something here???
    }

    @Override
    public boolean isInterpolating() {
        return G9utils.opt().cheats.opt().disableMultiTickInterpolation ? this.active : super.isInterpolating();
    }
}
