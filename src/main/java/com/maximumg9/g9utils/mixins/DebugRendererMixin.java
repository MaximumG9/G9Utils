package com.maximumg9.g9utils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.maximumg9.g9utils.renderers.DragonKBRenderer;
import com.maximumg9.g9utils.renderers.LaggedHitboxRenderer;
import com.maximumg9.g9utils.renderers.PearlPathRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin implements com.maximumg9.g9utils.DebugRendererMixinDuck {
    @Shadow
    @Final
    private List<DebugRenderer.Renderer> renderers;
    @Unique
    private LaggedHitboxRenderer laggedHitboxRenderer;

    @Inject(method = "initRenderers",at=@At("TAIL"))
    public void createOtherRenderers(CallbackInfo ci, @Local MinecraftClient client) {
        this.laggedHitboxRenderer = new LaggedHitboxRenderer(client);
        this.renderers.add(new DragonKBRenderer(client));
        this.renderers.add(new PearlPathRenderer(client));
        this.renderers.add(this.laggedHitboxRenderer);
    }

    @Unique
    @Override
    public LaggedHitboxRenderer g9Utils$getLaggedHitboxRenderer() {
        return laggedHitboxRenderer;
    }
}
