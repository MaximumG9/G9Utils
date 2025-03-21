package com.maximumg9.g9utils;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Supplier;

public interface InGameHudDuck {
    @Unique
    void g9Utils$addValue(Supplier<Text> getter, Text name, Supplier<Boolean> shouldRender);
}
