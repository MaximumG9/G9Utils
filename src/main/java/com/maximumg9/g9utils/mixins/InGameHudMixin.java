package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.renderers.G9HudLayer;
import com.maximumg9.g9utils.renderers.LineHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements com.maximumg9.g9utils.InGameHudDuck {
    @Shadow @Final private MinecraftClient client;
    // final but it won't let me mark it as final D:
    @Unique
    private G9HudLayer g9Hud;
    @Unique
    private LineHUD lineHUD;

    @Inject(at=@At("TAIL"), method="<init>")
    public void addG9UtilsLayer(MinecraftClient client, CallbackInfo ci) {
        this.g9Hud = new G9HudLayer(this.client.textRenderer);
        this.lineHUD = new LineHUD();
    }

    @Inject(method = "render",
        at= @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;renderPlayerList(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"
        )
    )
    public void renderHUDS(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        g9Hud.render(context, tickCounter);
        lineHUD.render(context, tickCounter);
    }

    @Override
    @Unique
    public void g9Utils$addValue(Supplier<Text> getter, Text name, Supplier<Boolean> shouldRender) {
        this.g9Hud.addValue(getter, name, shouldRender);
    }
}
