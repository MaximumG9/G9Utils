package com.maximumg9.g9utils.config.gui;

import com.maximumg9.g9utils.config.Keybind;
import com.maximumg9.g9utils.config.TypedField;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;

public class KeybindWidget extends net.minecraft.client.gui.widget.ButtonWidget.Text {
    private Keybind keybind;
    private final String name;
    private boolean isFocused;

    KeybindWidget(Keybind keybind, String bindingName, int x, int y, int width, int height) {
        super(x,
            y,
            width,
            height,
            getUnfocusedText(bindingName, keybind),
            KeybindWidget::clickHandler,
            ButtonWidget.DEFAULT_NARRATION_SUPPLIER
        );
        this.keybind = keybind;
        this.name = bindingName;
    }

    private static net.minecraft.text.Text getUnfocusedText(String bindingName, Keybind currentKey) {
        return net.minecraft.text.Text.literal(bindingName + ": ").append(currentKey.getLocalizedText());
    }


    private void updateText() {
        net.minecraft.text.Text txt = getUnfocusedText(this.name,this.keybind);
        if(!this.isFocused) {
            this.setMessage(txt);
        } else {
            this.setMessage(net.minecraft.text.Text.literal(">").append(txt).append("<"));
        }
    }

    private static void clickHandler(ButtonWidget self) {
        ((KeybindWidget) self).onClick();
    }

    private void onClick() {
        this.isFocused = true;
        updateText();
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        if(this.isFocused) {
            if(input.isEscape()) {
                this.keybind = Keybind.NONE;
            } else {
                this.keybind = Keybind.fromKey(input);
            }
            this.isFocused = false;
            updateText();
            return true;
        }
        super.keyReleased(input);
        return false;
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if(this.isFocused) {
            this.keybind = Keybind.fromMouse(click.buttonInfo());
            this.isFocused = false;
            updateText();
            return;
        }
        super.onClick(click, doubled);
    }

    public static <O>
    FieldWidget.FieldWidgetBuilder<KeybindWidget,Keybind,O>
    builder(
        TypedField<Keybind,O> field,
        Keybind kb,
        String name
    ) {
        return new FieldWidget.FieldWidgetBuilder<>(
            (x, y, width, height, name1) ->
                new KeybindWidget(
                    kb,
                    name1,
                    x,
                    y,
                    width,
                    height
                ),
            (configButton) -> configButton.keybind,
            field,
            name
        );
    }
}
