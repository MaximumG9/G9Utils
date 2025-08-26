package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.renderers.DragonKBRenderer;
import com.maximumg9.g9utils.renderers.LaggedHitboxRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    @Unique
    private LaggedHitboxRenderer laggedHitboxRenderer;
    @Unique
    private DragonKBRenderer dragonKBRenderer;

    @Inject(method = "<init>",at=@At("TAIL"))
    public void createOtherRenderers(MinecraftClient client, CallbackInfo ci) {
        this.laggedHitboxRenderer = new LaggedHitboxRenderer(client);
        this.dragonKBRenderer = new DragonKBRenderer(client);
    }

    @Inject(method="render",at=@At("TAIL"))
    public void renderOtherRenderers(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        this.laggedHitboxRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        this.dragonKBRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
    }
}
