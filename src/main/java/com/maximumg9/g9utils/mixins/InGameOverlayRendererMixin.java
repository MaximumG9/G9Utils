package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(method="renderFireOverlay",at=@At("HEAD"),cancellable = true)
    private static void renderFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        assert client.player != null;
        if( G9utils.getOptions().NoFireWhenResistant && (
                client.player.isInvulnerableTo(client.world.getDamageSources().onFire()) ||
                !client.player.canTakeDamage() ||
                client.player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)
            )) ci.cancel();
    }
}