package com.maximumg9.g9utils.config;

import com.maximumg9.g9utils.Util;
import com.maximumg9.g9utils.config.gui.FieldWidget;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OptionsScreen<O extends Options> extends Screen {
    private static final int WIDGET_WIDTH = 200;
    private static final int WIDGET_HEIGHT = 20;
    private static final int PADDING = 10;

    private final O option;
    private final Class<O> optionClass;
    private final List<FieldWidget<?,?,O>> widgets = new ArrayList<>();
    private final Screen parent;
    public KeyBinding selectedKeybind;

    public OptionsScreen(Screen parent, O option) {
        super(Text.of(Util.getClassStrict(option).getSimpleName()));
        this.optionClass = Util.getClassStrict(option);
        this.option = option;
        this.parent = parent;
    }

    public static boolean isValidField(TypedField<?,?> f, Object rootObj) {
        return (f.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) == 0 &&
            f.canAccess(rootObj);
    }

    @Override
    protected void init() {
        int x = PADDING;
        int y = PADDING;
        for(TypedField<?,O> field : (Iterable<TypedField<?,O>>) TypedField.getAllFields(this.optionClass)::iterator) {
            if(!isValidField(field, option)) continue;
            try {
                FieldWidget<?,?,O> fieldWidget = FieldWidget.createBuilder(field,option).dimensions(
                    x,y,
                    WIDGET_WIDTH,
                    WIDGET_HEIGHT
                ).buildFieldWidget();
                widgets.add(fieldWidget);
                this.addDrawableChild(fieldWidget.getWidget());

                x += WIDGET_WIDTH + PADDING;
                if(x + WIDGET_WIDTH > this.width) {
                    x = PADDING;
                    y += WIDGET_HEIGHT + PADDING;
                }
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    @Override
    public void close() {
        widgets.forEach((widget) -> {
            try {
                widget.save(option);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        Objects.requireNonNull(this.client).setScreen(this.parent);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        return super.keyPressed(input);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        return super.mouseClicked(click, doubled);
    }
}
