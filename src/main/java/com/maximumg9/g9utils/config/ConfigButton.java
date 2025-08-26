package com.maximumg9.g9utils.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ConfigButton<O extends Options> extends ButtonWidget {
    public final Config<O> config;

    public ConfigButton(
        Config<O> config,
        int x, int y,
        int width, int height,
        Text message,
        NarrationSupplier narrationSupplier
    ) {
        super(
            x, y,
            width, height,
            message,
            (button) -> ConfigButton.openConfig(config,button),
            narrationSupplier
        );
        this.config = config;
    }

    private static void openConfig(Config<?> config, ButtonWidget buttonWidget) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(
            new ConfigScreen<>(
                client.currentScreen,
                config
            )
        );
    }

    @Environment(EnvType.CLIENT)
    public static class Builder<O extends Options> {
        private final Text message;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private NarrationSupplier narrationSupplier;
        private final Config<O> config;

        public Builder(Text message, Config<O> config) {
            this.narrationSupplier = ButtonWidget.DEFAULT_NARRATION_SUPPLIER;
            this.message = message;
            this.config = config;
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

        public ConfigButton.Builder<O> tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public ConfigButton.Builder<O> narrationSupplier(NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public ConfigButton<O> build() {
            ConfigButton<O> buttonWidget = new ConfigButton<>(
                this.config,
                this.x, this.y,
                this.width, this.height,
                this.message,
                this.narrationSupplier
            );
            buttonWidget.setTooltip(this.tooltip);
            return buttonWidget;
        }
    }
}
