package com.maximumg9.g9utils.mixins;

import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoveredTooltipPositioner.class)
public class HoveredTooltipPositionerMixin {
    @Inject(method = "preventOverflow",at=@At("TAIL"))
    public void preventUpwardOverflow(int screenWidth, int screenHeight, Vector2i pos, int width, int height, CallbackInfo ci) {
        if (pos.y < 0) {
            pos.y = 0 + 3 + 1;
        }
    }
}
