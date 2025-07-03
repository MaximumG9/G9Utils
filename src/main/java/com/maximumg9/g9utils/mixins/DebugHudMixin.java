package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
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

    @Inject(method="method_51746",at=@At("TAIL"))
    public void render(DrawContext context, CallbackInfo ci) {
        if(!G9utils.getOptions().addRandomDigitsToF3) return;

        Random random = Random.create();

        int height = 9;

        for(int i=0;i<G9utils.getOptions().numRandomDigits;i++) {
            String string = String.valueOf(random.nextBetween(0,1000000000));
            int width = this.textRenderer.getWidth(string);
            int x = random.nextBetween(0,context.getScaledWindowWidth()-width);
            int y = random.nextBetween(0,context.getScaledWindowHeight()-height);

            context.drawText(this.textRenderer, string, x, y, 14737632, false);
        }
    }
}
