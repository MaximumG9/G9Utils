package com.maximumg9.g9utils.renderers;

import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.options.DragonOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DragonKBRenderer implements DebugRenderer.Renderer {
    private final MinecraftClient client;

    public DragonKBRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        DragonOptions dragonOpts = G9utils.opt().dragonOptions.opt();
        if(!dragonOpts.showDragonHitboxes) { return; }

        matrices.push();
        matrices.translate(-cameraX,-cameraY,-cameraZ);

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

            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLines());

            float kbRed = dragonOpts.kbRed / 255f;
            float kbGreen = dragonOpts.kbGreen / 255f;
            float kbBlue = dragonOpts.kbBlue / 255f;

            WorldRenderer.drawBox(
                matrices,
                consumer,
                leftWingKB,
                kbRed,
                kbGreen,
                kbBlue,
                1.0f
            );
            WorldRenderer.drawBox(
                matrices,
                consumer,
                rightWingKB,
                kbRed,
                kbGreen,
                kbBlue,
                1.0f
            );

            float dmgRed = dragonOpts.dmgRed / 255f;
            float dmgGreen = dragonOpts.dmgGreen / 255f;
            float dmgBlue = dragonOpts.dmgBlue / 255f;

            WorldRenderer.drawBox(
                matrices,
                consumer,
                headDmg,
                dmgRed,
                dmgGreen,
                dmgBlue,
                1.0f
            );
            WorldRenderer.drawBox(
                matrices,
                consumer,
                neckDmg,
                dmgRed,
                dmgGreen,
                dmgBlue,
                1.0f
            );

            double centerX = (dragon.body.getBoundingBox().minX + dragon.body.getBoundingBox().maxX) / 2;
            double centerZ = (dragon.body.getBoundingBox().minZ + dragon.body.getBoundingBox().maxZ) / 2;

            double bottomY = dragon.getBoundingBox().minY;
            double topY = dragon.getBoundingBox().maxY;

            consumer.vertex(
                matrices.peek(),
                (float) centerX,
                (float) bottomY,
                (float) centerZ
            ).color(
                dragonOpts.centerRed,
                dragonOpts.centerGreen,
                dragonOpts.centerBlue,
                255
            ).normal(0,-1,0);

            consumer.vertex(
                matrices.peek(),
                (float) centerX,
                (float) topY,
                (float) centerZ
            ).color(
                dragonOpts.centerRed,
                dragonOpts.centerGreen,
                dragonOpts.centerBlue,
                255
            ).normal(0,1,0);

            drawCircle(
                matrices,
                consumer,
                new Vec3d(
                    centerX,
                    (
                        dragon.body.getBoundingBox().minY +
                        dragon.body.getBoundingBox().maxY
                    ) / 2,
                    centerZ
                ),
                (float) Math.sqrt(0.1),
                dragonOpts.optimalRed/255f,
                dragonOpts.optimalGreen/255f,
                dragonOpts.optimalBlue/255f,
                1f,
                32
            );
        }

        matrices.pop();
    }

    public static void drawCircle(
        MatrixStack matricies,
        VertexConsumer consumer,
        Vec3d pos,
        float radius,
        float red,
        float green,
        float blue,
        float alpha,
        int sides
    ) {
        matricies.push();
        matricies.translate(pos.x,pos.y,pos.z);
        MatrixStack.Entry matrix = matricies.peek();

        consumer.vertex(
                matrix,
                radius,
                0,
                0
            )
            .color(red, green, blue, alpha)
            .normal(
                0,
                0,
                1
            );

        for (float i = MathHelper.TAU/sides; i < MathHelper.TAU; i += MathHelper.TAU/sides) {
            consumer.vertex(
                matrix,
                radius * MathHelper.cos(i),
                0,
                radius * MathHelper.sin(i)
            )
                .color(red, green, blue, alpha)
                .normal(
                    -MathHelper.sin(i),
                    0,
                    MathHelper.cos(i)
                );

            consumer.vertex(
                    matrix,
                    radius * MathHelper.cos(i),
                    0,
                    radius * MathHelper.sin(i)
                )
                .color(red, green, blue, alpha)
                .normal(
                    -MathHelper.sin(i),
                    0,
                    MathHelper.cos(i)
                );

        }
        consumer.vertex(
            matrix,
            radius,
            0,
            0
        ).color(red, green, blue, alpha)
            .normal(
                0,
                0,
                1
            );
        matricies.pop();
    }
}
