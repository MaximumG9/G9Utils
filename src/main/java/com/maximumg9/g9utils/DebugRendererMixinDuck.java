package com.maximumg9.g9utils;

import com.maximumg9.g9utils.renderers.InterpolationRenderer;
import org.spongepowered.asm.mixin.Unique;

public interface DebugRendererMixinDuck {
    @Unique
    InterpolationRenderer g9Utils$getInterlopationHitboxRenderer();
}
