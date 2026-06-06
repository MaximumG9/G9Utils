package com.maximumg9.g9utils.renderers;

import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.LinearPositionalInterpolator;
import com.maximumg9.g9utils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.PositionInterpolator;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

import java.util.Objects;

public class InterpolationRenderer implements DebugRenderer.Renderer {
    private final MinecraftClient client;

    private Vec3d clientPlayerInterpPos = null;
    private Vec3d lastClientPlayerInterpPos = null;

    private float interpYaw = 0;
    private float interpPitch = 0;

    public InterpolationRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum, float tickProgress) {
        if(!G9utils.opt().rendering.visualizeInterpolation) return;
        if(this.client.world == null) return;
        for(Entity entity : client.world.getEntities()) {
            if (entity.isInvisible() ||
                !frustum.isVisible(entity.getBoundingBox()) ||
                entity == this.client.getCameraEntity() &&
                    this.client.options.getPerspective() == Perspective.FIRST_PERSON
            ) continue;
            if(entity.isInterpolating()) {
                PositionInterpolator interpolator = entity.getInterpolator();
                Objects.requireNonNull(interpolator);
                if(interpolator instanceof LinearPositionalInterpolator lin &&
                    G9utils.opt().cheats.disableMultiTickInterpolation) {
                    visualizeLinearInterpolation(entity, lin);
                } else {
                    visualizeNormalInterpolation(entity, interpolator);
                }
            } else if(entity == client.player && G9utils.opt().rendering.visualizeOwnInterpolation) {
                int color = ColorHelper.getArgb(255,0,255,0);

                if(clientPlayerInterpPos == null) continue;

                Vec3d frameInterpedPos;

                if(lastClientPlayerInterpPos != null) {
                    frameInterpedPos = Util.interpVec(tickProgress,lastClientPlayerInterpPos,clientPlayerInterpPos);
                } else {
                    frameInterpedPos = clientPlayerInterpPos;
                }

                Vec3d posDiff = frameInterpedPos.subtract(entity.getEntityPos());

                GizmoDrawing.box(
                    entity.getBoundingBox().offset(posDiff),
                    DrawStyle.stroked(color)
                );

                Vec3d lastEyePos = frameInterpedPos.add(0,entity.getStandingEyeHeight(),0);

                GizmoDrawing.arrow(
                    lastEyePos,
                    lastEyePos.add(
                        entity.getRotationVector(interpPitch, interpYaw)
                            .multiply(2)
                    ),
                    color
                );

            }
        }
    }

    private static void visualizeNormalInterpolation(Entity entity, PositionInterpolator interpolator) {
        {
            PositionInterpolator.Data interpolationData = Objects.requireNonNull(
                interpolator
            ).data;

            Vec3d pos = interpolationData.pos;
            float yaw = interpolationData.yaw;
            float pitch = interpolationData.pitch;

            float timeFraction = (float) interpolationData.step / interpolator.lerpDuration;

            int finalColor = ColorHelper.getArgb(255,0,255,0);
            int startColor = ColorHelper.getArgb(255,0,0,255);

            int currentColor = ColorHelper.lerp(timeFraction,startColor,finalColor);

            Vec3d posDiff = pos.subtract(entity.getEntityPos());

            GizmoDrawing.box(
                entity.getBoundingBox().offset(posDiff),
                DrawStyle.stroked(currentColor)
            );

            Vec3d eyePos = pos.add(0,entity.getStandingEyeHeight(),0);

            GizmoDrawing.arrow(
                eyePos,
                eyePos.add(
                    entity.getRotationVector(pitch,yaw).multiply(3)
                ),
                currentColor
            );
        }
        {
            int color = ColorHelper.getArgb(0,0,255);

            Vec3d pos = Vec3d.ZERO;

            if(interpolator.lastPos != null) {
                pos = entity.getEntityPos();
                Vec3d posDiff = interpolator.lastPos.subtract(pos);
                GizmoDrawing.box(
                    entity.getBoundingBox().offset(posDiff),
                    DrawStyle.stroked(color)
                );
            }

            if(interpolator.lastRotation != null) {
                Vec3d lastEyePos = pos.add(0,entity.getStandingEyeHeight(),0);
                float yaw = interpolator.lastRotation.y;
                float pitch = interpolator.lastRotation.x;

                GizmoDrawing.arrow(
                    lastEyePos,
                    lastEyePos.add(
                        entity.getRotationVector(pitch, yaw)
                            .multiply(3)
                    ),
                    color
                );
            }
        }
    }

    private static void visualizeLinearInterpolation(Entity entity, LinearPositionalInterpolator interpolator) {
        if(interpolator.prevRecvdPos != null) {
            int startColor = ColorHelper.getArgb(255,0,0,0);
            Vec3d posDiff = interpolator.prevRecvdPos.subtract(entity.getEntityPos());

            GizmoDrawing.box(
                entity.getBoundingBox().offset(posDiff),
                DrawStyle.stroked(startColor)
            );

            Vec3d eyePos = interpolator.prevRecvdPos.add(0,entity.getStandingEyeHeight(),0);

            GizmoDrawing.arrow(
                eyePos,
                eyePos.add(
                    entity.getRotationVector(
                        interpolator.prevRecvdPitch,
                        interpolator.prevRecvdYaw
                    ).multiply(2)
                ),
                startColor
            );
        }
        if(interpolator.lastRecvdPos != null) {
            int finalColor = ColorHelper.getArgb(255,0,255,0);
            Vec3d posDiff = interpolator.lastRecvdPos.subtract(entity.getEntityPos());

            GizmoDrawing.box(
                entity.getBoundingBox().offset(posDiff),
                DrawStyle.stroked(finalColor)
            );

            Vec3d eyePos = interpolator.lastRecvdPos.add(0,entity.getStandingEyeHeight(),0);

            GizmoDrawing.arrow(
                eyePos,
                eyePos.add(
                    entity.getRotationVector(
                        interpolator.lastRecvdPitch,
                        interpolator.lastRecvdYaw
                    ).multiply(2)
                ),
                finalColor
            );
        }
    }

    public void tickNewClientPlayerPos(Vec3d newClientPlayerPos, float newPitch, float newYaw) {
        float delta = (float) 1 /PositionInterpolator.DEFAULT_INTERPOLATION_DURATION;
        lastClientPlayerInterpPos = clientPlayerInterpPos;

        if(this.clientPlayerInterpPos == null) {
            this.clientPlayerInterpPos = newClientPlayerPos;
        } else {
            this.clientPlayerInterpPos = Util.interpVec(delta, this.clientPlayerInterpPos, newClientPlayerPos);
        }

        this.interpPitch = MathHelper.lerp(delta, this.interpPitch, newPitch);
        this.interpYaw = MathHelper.lerp(delta, this.interpYaw, newYaw);
    }
}
