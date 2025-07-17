package com.maximumg9.g9utils;

import org.spongepowered.asm.mixin.Unique;

public interface ClientCommonNetworkHandlerMixinDuck {
    @Unique
    boolean g9Utils$isServerSideSprinting();
    @Unique
    void g9Utils$setServerSideSprinting(boolean sprinting);
}
