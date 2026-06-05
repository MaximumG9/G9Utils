package com.maximumg9.g9utils.config;


import net.minecraft.client.input.AbstractInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.input.SystemKeycodes;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public record Keybind(InputUtil.Key key, @AbstractInput.Modifier int modifiers) {
    public static Keybind NONE = new Keybind(InputUtil.UNKNOWN_KEY,0);

    public Text getLocalizedText() {
        if(this == NONE) {
            return Text.literal("None");
        } else {
            return getModifiersText().append(key.getLocalizedText());
        }
    }

    public static Keybind fromKeycode(int code) {
        return new Keybind(InputUtil.Type.KEYSYM.createFromCode(code),0);
    }

    public static Keybind fromMousecode(int code) {
        return new Keybind(InputUtil.Type.MOUSE.createFromCode(code),0);
    }

    public static Keybind fromCodeWModifiers(InputUtil.Type type, int code, int modifiers) {
        return new Keybind(type.createFromCode(code),modifiers);
    }

    public static Keybind fromKey(KeyInput keyInput) {
        return new Keybind(
            InputUtil.Type.KEYSYM
                .createFromCode(
                    keyInput.key()
                ),
            keyInput.modifiers()
        );
    }

    public static Keybind fromMouse(MouseInput mouseInput) {
        return new Keybind(
            InputUtil.Type.MOUSE
                .createFromCode(
                    mouseInput.button()
                ),
            mouseInput.modifiers()
        );
    }

    private static final Text CTRL_TEXT = InputUtil.Type.KEYSYM
        .createFromCode(InputUtil.GLFW_KEY_LEFT_CONTROL)
        .getLocalizedText();
    private static final Text SHIFT_TEXT = InputUtil.Type.KEYSYM
        .createFromCode(InputUtil.GLFW_KEY_LEFT_SHIFT)
        .getLocalizedText();
    private static final Text ALT_TEXT = InputUtil.Type.KEYSYM
        .createFromCode(InputUtil.GLFW_KEY_LEFT_ALT)
        .getLocalizedText();
    private static final Text SUPER_TEXT = InputUtil.Type.KEYSYM
        .createFromCode(InputUtil.GLFW_KEY_LEFT_SUPER)
        .getLocalizedText();


    public MutableText getModifiersText() {
        MutableText text = Text.empty();

        if(hasCtrlOrCmd()) {
            text.append(CTRL_TEXT);
            text.append(Text.literal("+"));
        }
        if(hasShift()) {
            text.append(SHIFT_TEXT);
            text.append(Text.literal("+"));
        }
        if(hasAlt()) {
            text.append(ALT_TEXT);
            text.append(Text.literal("+"));
        }
        if(hasSuper()) {
            text.append(SUPER_TEXT);
            text.append(Text.literal("+"));
        }
        return text;
    }

    public boolean hasAlt() {
        return (this.modifiers() & InputUtil.GLFW_MOD_ALT) != 0;
    }

    public boolean hasShift() {
        return (this.modifiers() & InputUtil.GLFW_MOD_SHIFT) != 0;
    }

    public boolean hasSuper() {
        return (this.modifiers() & InputUtil.GLFW_MOD_SUPER) != 0;
    }

    public boolean hasCtrlOrCmd() {
        return (this.modifiers() & SystemKeycodes.CTRL_MOD) != 0;
    }
}