package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.renderers.DragonKBRenderer;
import com.maximumg9.g9utils.renderers.LaggedHitboxRenderer;
import com.maximumg9.g9utils.renderers.PearlPathRenderer;
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
public class DebugRendererMixin implements com.maximumg9.g9utils.DebugRendererMixinDuck {
    @Unique
    private LaggedHitboxRenderer laggedHitboxRenderer;
    @Unique
    private DragonKBRenderer dragonKBRenderer;
    @Unique
    private PearlPathRenderer pearlPathRenderer;

    @Inject(method = "<init>",at=@At("TAIL"))
    public void createOtherRenderers(MinecraftClient client, CallbackInfo ci) {
        this.laggedHitboxRenderer = new LaggedHitboxRenderer(client);
        this.dragonKBRenderer = new DragonKBRenderer(client);
        this.pearlPathRenderer = new PearlPathRenderer(client);
    }

    @Unique
    @Override
    public LaggedHitboxRenderer g9Utils$getLaggedHitboxRenderer() {
        return laggedHitboxRenderer;
    }

    @Inject(method="render",at=@At("TAIL"))
    public void renderOtherRenderers(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        this.laggedHitboxRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        this.dragonKBRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        this.pearlPathRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
    }
}
