package com.maximumg9.g9utils.config.gui;

import com.google.common.collect.ImmutableList;
import com.maximumg9.g9utils.config.OptionsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class KeyBindingWidget implements ParentElement {
    private static final Text RESET_TEXT = Text.translatable("controls.reset");
    private final KeyBinding binding;
    private final Text bindingName;
    private final ButtonWidget editButton;
    private final ButtonWidget resetButton;
    private boolean duplicate = false;
    private final OptionsScreen<?> parent;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private Element focusedElement;
    private boolean dragging;

    KeyBindingWidget(KeyBinding binding, Text bindingName, OptionsScreen<?> parent, int x, int y, int width, int height) {
        this.parent = parent;
        this.binding = binding;
        this.bindingName = bindingName;
        this.editButton = ButtonWidget.builder(bindingName, button -> {
            this.parent.selectedKeybind = binding;
            this.parent.update();
        }).dimensions(0, 0, 75, 20)
        .narrationSupplier(textSupplier -> {
            if (binding.isUnbound()) {
                return Text.translatable("narrator.controls.unbound", bindingName);
            }
            return Text.translatable("narrator.controls.bound", bindingName, textSupplier.get());
        }).build();
        this.resetButton = ButtonWidget.builder(RESET_TEXT, button -> {
            binding.setBoundKey(binding.getDefaultKey());
            this.parent.update();
        }).dimensions(0, 0, 50, 20)
            .narrationSupplier(textSupplier -> Text.translatable("narrator.controls.reset", bindingName))
            .build();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.update();
    }

    private int getContentX() {
        return x + 2;
    }
    private int getContentY() {
        return y + 2;
    }

    public int getContentHeight() {
        return this.height - 4;
    }

    public int getContentMiddleY() {
        return this.getContentY() + this.getContentHeight() / 2;
    }

    public int getContentBottomEnd() {
        return this.getContentY() + this.getContentHeight();
    }

    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        int i = -this.resetButton.getWidth() - 10;
        int j = this.getContentY() - 2;
        this.resetButton.setPosition(i, j);
        this.resetButton.render(context, mouseX, mouseY, deltaTicks);
        int k = i - 5 - this.editButton.getWidth();
        this.editButton.setPosition(k, j);
        this.editButton.render(context, mouseX, mouseY, deltaTicks);
        context.drawTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            this.bindingName,
            this.getContentX(),
            this.getContentMiddleY() - MinecraftClient.getInstance().textRenderer.fontHeight / 2,
            Colors.WHITE
        );
        if (this.duplicate) {
            int m = this.editButton.getX() - 6;
            context.fill(m, this.getContentY() - 1, m + 3, this.getContentBottomEnd(), Colors.YELLOW);
        }
    }

    public void renderChildren(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        this.editButton.render(context, mouseX, mouseY, deltaTicks);
        this.resetButton.render(context, mouseX, mouseY, deltaTicks);
    }


    public List<? extends Selectable> selectableChildren() {
        return ImmutableList.of(this.editButton, this.resetButton);
    }


    protected void update() {
        this.editButton.setMessage(this.binding.getBoundKeyLocalizedText());
        this.resetButton.active = !this.binding.isDefault();
        this.duplicate = false;
        MutableText mutableText = Text.empty();
        if (!this.binding.isUnbound()) {
            for (KeyBinding keyBinding : MinecraftClient.getInstance().options.allKeys) {
                if (keyBinding == this.binding || !this.binding.equals(keyBinding) || keyBinding.isDefault() && this.binding.isDefault()) continue;
                if (this.duplicate) {
                    mutableText.append(", ");
                }
                this.duplicate = true;
                mutableText.append(Text.translatable(keyBinding.getId()));
            }
        }
        if (this.duplicate) {
            this.editButton.setMessage(Text.literal("[ ").append(this.editButton.getMessage().copy().formatted(Formatting.WHITE)).append(" ]").formatted(Formatting.YELLOW));
            this.editButton.setTooltip(Tooltip.of(Text.translatable("controls.keybinds.duplicateKeybinds", mutableText)));
        } else {
            this.editButton.setTooltip(null);
        }
        if (this.parent.selectedKeybind == this.binding) {
            this.editButton.setMessage(Text.literal("> ").append(this.editButton.getMessage().copy().formatted(Formatting.WHITE, Formatting.UNDERLINE)).append(" <").formatted(Formatting.YELLOW));
        }
    }

    @Override
    public List<? extends Element> children() {
        return ImmutableList.of(this.editButton, this.resetButton);
    }

    @Override
    public boolean isDragging() {
        return this.dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public @Nullable Element getFocused() {
        return this.focusedElement;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        this.focusedElement = focused;
    }
}
