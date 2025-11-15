package com.maximumg9.g9utils.renderers;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PearlPathRenderer implements DebugRenderer.Renderer {
    private final MinecraftClient client;

    public PearlPathRenderer(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        if(!G9utils.opt().seeExpectedRandomlessPearl) return;

        ClientPlayerEntity cPlayer = client.player;

        if(cPlayer == null) return;
        if(client.world == null) return;

        if(!cPlayer.getMainHandStack().isOf(Items.ENDER_PEARL)) return;

        FakePearl rootPearl = new FakePearl(client.world, cPlayer);

        rootPearl.setItem(cPlayer.getMainHandStack());
        rootPearl.setVelocitySortOf(cPlayer.getPitch(), cPlayer.getYaw(), 0.0f, 1.5f, Vec3d.ZERO);

        int i = 0;

        while(rootPearl.tpPos == null && i < 128) {
            rootPearl.tick();
            i++;
        }

        if(rootPearl.tpPos == null) return;

        Vec3d landPos = rootPearl.tpPos;

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLines());

        matrices.push();
        matrices.translate(-cameraX,-cameraY,-cameraZ);

        consumer.vertex(
                matrices.peek(),
                landPos.subtract(0,0.5,0).toVector3f()
            ).color(0f,1f,0f,1f)
            .normal(0f,1f,0f);

        consumer.vertex(
                matrices.peek(),
                landPos.add(0,0.5,0).toVector3f()
            ).color(0f,1f,0f,1f)
            .normal(0f,-1f,0f);

        consumer.vertex(
                matrices.peek(),
                landPos.subtract(0.5,0,0).toVector3f()
            ).color(1f,0f,0f,1f)
            .normal(1f,0f,0f);

        consumer.vertex(
                matrices.peek(),
                landPos.add(0.5,0,0).toVector3f()
            ).color(1f,0f,0f,1f)
            .normal(-1f,0f,0f);

        consumer.vertex(
                matrices.peek(),
                landPos.subtract(0,0,0.5).toVector3f()
            ).color(0f,0f,1f,1f)
            .normal(0f,0f,1f);

        consumer.vertex(
                matrices.peek(),
                landPos.add(0,0,0.5).toVector3f()
            ).color(0f,0f,1,1f)
            .normal(0f,0f,-1f);

        matrices.pop();
    }

    private static class FakePearl extends EnderPearlEntity {

        private Vec3d tpPos = null;

        public FakePearl(EntityType<? extends EnderPearlEntity> entityType, World world) {
            super(entityType, world);
        }

        public FakePearl(World world, LivingEntity owner) {
            super(world, owner);
        }

        @Override
        protected void onCollision(HitResult hitResult) {
            if(this.tpPos == null) {
                this.tpPos = this.getPos();
            }
        }

        public void setVelocitySortOf(float pitch, float yaw, float roll, float speed, Vec3d divergence) {
            float x = -MathHelper.sin(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180));
            float y = -MathHelper.sin((pitch + roll) * ((float)Math.PI / 180));
            float z = MathHelper.cos(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180));
            Vec3d vec3d = new Vec3d(x, y, z).normalize().add(divergence).multiply(speed);
            this.setVelocity(vec3d);
            this.velocityDirty = true;
            double d = vec3d.horizontalLength();
            this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
            this.setPitch((float)(MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
    }
}
