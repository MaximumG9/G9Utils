package com.maximumg9.g9utils.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;

public class ConfigButton<O extends Options> extends ButtonWidget.Text {
    public final O options;

    public ConfigButton(
        O options,
        int x, int y,
        int width, int height,
        net.minecraft.text.Text message,
        NarrationSupplier narrationSupplier
    ) {
        super(
            x, y,
            width, height,
            message,
            (button) -> {
                MinecraftClient client = MinecraftClient.getInstance();
                client.setScreen(
                    new OptionsScreen<>(
                        client.currentScreen,
                        options
                    )
                );
            },
            narrationSupplier
        );
        this.options = options;
    }

    @Environment(EnvType.CLIENT)
    public static class Builder<O extends Options> {
        private final net.minecraft.text.Text message;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private final NarrationSupplier narrationSupplier;
        private final O options;

        public Builder(net.minecraft.text.Text message, O options) {
            this.narrationSupplier = ButtonWidget.DEFAULT_NARRATION_SUPPLIER;
            this.message = message;
            this.options = options;
        }

        public ConfigButton.Builder<O> position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public ConfigButton.Builder<O> size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public ConfigButton.Builder<O> dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public ConfigButton<O> build() {
            return new ConfigButton<>(
                this.options,
                this.x, this.y,
                this.width, this.height,
                this.message,
                this.narrationSupplier
            );
        }
    }
}
