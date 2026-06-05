package com.maximumg9.g9utils;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

public interface GameRendererMixinDuck {
    @Unique
    void g9Utils$setFOVOverride(@Nullable Float fovOverride);
}
