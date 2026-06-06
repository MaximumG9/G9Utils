package com.maximumg9.g9utils.config;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.input.SystemKeycodes;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.OptionalInt;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;

public class Keybind {
    public final InputUtil.Key key;
    public final @AbstractInput.Modifier int modifiers;

    // NOT SAVED AND NO NEED TO COPY!!!
    private int timesPressed;
    private boolean pressed;

    private static final LoadingCache<Integer, Set<Keybind>> bindsByCode = CacheBuilder.newBuilder()
        .build(
        new CacheLoader<>() {
            @Override
            public @NonNull Set<Keybind> load(@NonNull Integer key) {
                return Collections.newSetFromMap(new WeakHashMap<>());
            }
        }
    );

    public static void onKeyPressed(KeyInput input) {
        try {
            Set<Keybind> correspondingBinds = bindsByCode.get(input.key());
            OptionalInt usedModifiers = correspondingBinds.stream()
                .mapToInt((bind) -> bind.modifiers)
                .reduce((i1,i2) -> i1 | i2);
            if(usedModifiers.isEmpty()) return;

            correspondingBinds.forEach((keybind -> {
                if((input.modifiers() & usedModifiers.getAsInt()) == keybind.modifiers) {
                    keybind.timesPressed++;
                }
            }));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setKeyPressed(KeyInput input, boolean pressed) {
        try {
            Set<Keybind> correspondingBinds = bindsByCode.get(input.key());
            OptionalInt usedModifiers = correspondingBinds.stream().mapToInt((bind) -> bind.modifiers)
                .reduce((i1,i2) -> i1 | i2);
            if(usedModifiers.isEmpty()) return;

            correspondingBinds.forEach((keybind -> {
                if((input.modifiers() & usedModifiers.getAsInt()) == keybind.modifiers) {
                    keybind.pressed = pressed;
                }
            }));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onMousePressed(MouseInput input) {
        try {
            Set<Keybind> correspondingBinds = bindsByCode.get(input.button());
            OptionalInt usedModifiers = correspondingBinds.stream().mapToInt((bind) -> bind.modifiers)
                .reduce((i1,i2) -> i1 | i2);
            if(usedModifiers.isEmpty()) return;

            correspondingBinds.forEach((keybind -> {
                if((input.modifiers() & usedModifiers.getAsInt()) == keybind.modifiers) {
                    keybind.timesPressed++;
                }
            }));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setMousePressed(MouseInput input, boolean pressed) {
        try {
            Set<Keybind> correspondingBinds = bindsByCode.get(input.button());
            OptionalInt usedModifiers = correspondingBinds.stream().mapToInt((bind) -> bind.modifiers)
                .reduce((i1,i2) -> i1 | i2);
            if(usedModifiers.isEmpty()) return;

            correspondingBinds.forEach((keybind -> {
                if((input.modifiers() & usedModifiers.getAsInt()) == keybind.modifiers) {
                    keybind.pressed = pressed;
                }
            }));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Keybind(InputUtil.Key key, @AbstractInput.Modifier int modifiers) {
        this.key = key;
        this.modifiers = modifiers;
        try {
            bindsByCode.get(this.key.getCode()).add(this);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        if(this == NONE) return;
        try {
            bindsByCode.get(this.key.getCode()).remove(this);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        this.timesPressed = Integer.MIN_VALUE;
        this.pressed = false;
    }

    public static Keybind NONE = new Keybind(InputUtil.UNKNOWN_KEY,0);

    public Text getLocalizedText() {
        if(this == NONE) {
            return Text.literal("None");
        } else {
            return getModifiersText().append(key.getLocalizedText());
        }
    }

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

    public boolean isPressed() { return pressed; }
    public boolean wasPressed() {
        if(timesPressed > 0) {
            timesPressed--;
            return true;
        }
        return false;
    }

    public boolean hasAlt() { return (this.modifiers & InputUtil.GLFW_MOD_ALT) != 0; }
    public boolean hasShift() { return (this.modifiers & InputUtil.GLFW_MOD_SHIFT) != 0; }
    public boolean hasSuper() { return (this.modifiers & InputUtil.GLFW_MOD_SUPER) != 0; }
    public boolean hasCtrlOrCmd() { return (this.modifiers & SystemKeycodes.CTRL_MOD) != 0; }

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

    private static final Text CTRL_TEXT = Text.translatable("g9utils.modifier.keyboard.control");
    private static final Text SHIFT_TEXT = Text.translatable("g9utils.modifier.keyboard.shift");
    private static final Text ALT_TEXT = Text.translatable("g9utils.modifier.keyboard.alt");
    private static final Text SUPER_TEXT = Text.translatable("g9utils.modifier.keyboard.super");
}