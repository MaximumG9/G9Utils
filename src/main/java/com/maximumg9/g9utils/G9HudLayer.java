package com.maximumg9.g9utils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class G9HudLayer implements LayeredDrawer.Layer {
    private final List<Value> values = new ArrayList<>();

    private final TextRenderer textRenderer;

    public G9HudLayer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    public void addValue(Supplier<Text> getter, Text name, Supplier<Boolean> shouldRender) {
        values.add(new Value(getter,name, shouldRender));
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        int y = 0;

        for(Value value : values) {
            if(!value.shouldRender.get()) continue;
            Text text = value.name.copy().append(value.getter().get());

            int x = context.getScaledWindowWidth() - textRenderer.getWidth(text);
            context.drawText(textRenderer, text, x, y, Colors.WHITE, true);
            y += textRenderer.fontHeight;
        }

    }

    private record Value(Supplier<Text> getter, Text name, Supplier<Boolean> shouldRender) {}
}
