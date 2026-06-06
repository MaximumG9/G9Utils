package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.Colors;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
    @Shadow @Final private TextRenderer textRenderer;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method="render", at= @At("TAIL"))
    public void render(DrawContext context, CallbackInfo ci) {
        if(!G9utils.opt().useless.addRandomDigitsToF3) return;
        if(!this.client.debugHudEntryList.isF3Enabled()) return;

        Random random = Random.create();

        int height = 9;

        for(int i = 0; i<G9utils.opt().useless.numRandomDigits; i++) {
            String string = String.valueOf(random.nextBetween(0,9));
            int width = this.textRenderer.getWidth(string);
            int x = random.nextBetween(0,context.getScaledWindowWidth()-width);
            int y = random.nextBetween(0,context.getScaledWindowHeight()-height);

            context.fill(x - 1, y - 1, x + width + 1, y + height - 1, -1873784752);
            context.drawText(this.textRenderer, string, x, y, Colors.LIGHTER_GRAY, false);
        }
    }
}
