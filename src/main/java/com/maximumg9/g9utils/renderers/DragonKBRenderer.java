package com.maximumg9.g9utils.renderers;

import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.options.DragonOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.DebugDataStore;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

public class DragonKBRenderer implements DebugRenderer.Renderer {
    private final MinecraftClient client;

    public DragonKBRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(double cameraX,
                       double cameraY,
                       double cameraZ,
                       DebugDataStore store,
                       Frustum frustum,
                       float tickProgress) {
        DragonOptions dragonOpts = G9utils.opt().dragonOptions.opt();
        if(!dragonOpts.showDragonHitboxes) { return; }

        if(client.world == null) return;

        for(Entity e : client.world.getEntities()) {
            if(!(e instanceof EnderDragonEntity dragon)) continue;

            Box leftWingKB = dragon.leftWing
                .getBoundingBox()
                .expand(4.0, 2.0, 4.0)
                .offset(0.0, -2.0, 0.0);
            Box rightWingKB = dragon.rightWing
                .getBoundingBox()
                .expand(4.0, 2.0, 4.0)
                .offset(0.0, -2.0, 0.0);

            Box headDmg = dragon.head
                .getBoundingBox()
                .expand(1.0);
            Box neckDmg = dragon.neck
                .getBoundingBox()
                .expand(1.0);

            DrawStyle kbStyle = DrawStyle.stroked(
                ColorHelper.getArgb(
                    dragonOpts.kbRed,
                    dragonOpts.kbGreen,
                    dragonOpts.kbBlue
                )
            );

            GizmoDrawing.box(
                leftWingKB,
                kbStyle
            );
            GizmoDrawing.box(
                rightWingKB,
                kbStyle
            );

            DrawStyle dmgStyle = DrawStyle.stroked(
                ColorHelper.getArgb(
                    dragonOpts.dmgRed,
                    dragonOpts.dmgGreen,
                    dragonOpts.dmgBlue
                )
            );

            GizmoDrawing.box(
                headDmg,
                dmgStyle
            );
            GizmoDrawing.box(
                neckDmg,
                dmgStyle
            );

            double centerX = (dragon.body.getBoundingBox().minX + dragon.body.getBoundingBox().maxX) / 2;
            double centerZ = (dragon.body.getBoundingBox().minZ + dragon.body.getBoundingBox().maxZ) / 2;

            double bottomY = dragon.getBoundingBox().minY;
            double topY = dragon.getBoundingBox().maxY;

            GizmoDrawing.circle(
                new Vec3d(centerX,bottomY,centerZ),
                (float) Math.sqrt(0.1),
                DrawStyle.stroked(
                    ColorHelper.getArgb(
                        dragonOpts.optimalRed,
                        dragonOpts.optimalGreen,
                        dragonOpts.optimalBlue
                    )
                )
            );

            GizmoDrawing.line(
                new Vec3d(centerX,bottomY,centerZ),
                new Vec3d(centerX,topY,centerZ),
                ColorHelper.getArgb(
                    dragonOpts.centerRed,
                    dragonOpts.centerGreen,
                    dragonOpts.centerBlue
                )
            );
        }
    }
}
