package com.maximumg9.g9utils;

import org.spongepowered.asm.mixin.Unique;

public interface PlayerMixinDuck {
    boolean g9Utils$wasAirborneLastFrame();

    @Unique
    double g9Utils$getLastCurrentSpeed();
}
