package com.maximumg9.g9utils;

import com.maximumg9.g9utils.renderers.LaggedHitboxRenderer;
import org.spongepowered.asm.mixin.Unique;

public interface DebugRendererMixinDuck {
    @Unique
    LaggedHitboxRenderer g9Utils$getLaggedHitboxRenderer();
}
