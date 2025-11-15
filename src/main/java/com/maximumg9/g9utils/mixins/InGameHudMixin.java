package com.maximumg9.g9utils.mixins;

import com.maximumg9.g9utils.renderers.G9HudLayer;
import com.maximumg9.g9utils.renderers.LineHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Supplier;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements com.maximumg9.g9utils.InGameHudDuck {
    @Shadow @Final private MinecraftClient client;
    // final but it won't let me mark it as final D:
    @Unique
    private G9HudLayer g9Hud;

    @ModifyVariable(at=@At("STORE"), method="<init>",ordinal = 0)
    public LayeredDrawer addG9UtilsLayer(LayeredDrawer value) {
        this.g9Hud = new G9HudLayer(this.client.textRenderer);
        value.addLayer(this.g9Hud);
        LineHUD lineHUD = new LineHUD();
        value.addLayer(lineHUD);
        return value;
    }

    @Override
    @Unique
    public void g9Utils$addValue(Supplier<Text> getter, Text name, Supplier<Boolean> shouldRender) {
        this.g9Hud.addValue(getter, name, shouldRender);
    }
}
