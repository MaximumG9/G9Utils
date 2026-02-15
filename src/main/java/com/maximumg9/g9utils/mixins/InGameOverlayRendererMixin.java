package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Redirect(method="renderOverlays",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isOnFire()Z"))
    private boolean renderFireOverlay(ClientPlayerEntity instance) {
        if(!instance.isOnFire()) return false;
        if(this.client.world == null) return false;
        if(this.client.player == null) return false;
        return !G9utils.opt().NoFireWhenResistant || (client.player.canTakeDamage() &&
            !client.player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE));
    }
}