package com.maximumg9.g9utils;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum SwordHitType {
    REDIRECT(
        Text.literal("Redirect")
        .styled(
            style -> style.withColor(Formatting.GOLD)
        )
    ),
    SWEEP(
        Text.literal("Sweep")
            .styled(
                style -> style.withColor(Formatting.RED)
            )
    ),
    SPRINT(
        Text.literal("Sprint")
            .styled(
                style -> style.withColor(Formatting.GREEN)
            )
    ),
    CRIT(
        Text.literal("Critical")
            .styled(
                style -> style.withColor(Formatting.GRAY)
            )
    ),
    NORMAL(
        Text.literal("Normal")
            .styled(
                style -> style.withColor(Formatting.BLUE)
            )
    );

    public final Text text;

    SwordHitType(Text text) {
        this.text = text;
    }
}
