package com.maximumg9.g9utils.config;

import com.maximumg9.g9utils.Util;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OptionsScreen<O extends Options> extends Screen {
    private static final int WIDGET_WIDTH = 200;
    private static final int WIDGET_HEIGHT = 20;
    private static final int PADDING = 10;

    private final O option;
    private final Class<O> configClass;
    private final List<FieldWidget<?,?>> widgets = new ArrayList<>();
    private final Screen parent;

    public OptionsScreen(Screen parent, O option) {
        super(Text.of(Util.getClassStrict(option).getSimpleName()));
        this.configClass = Util.getClassStrict(option);
        this.option = option;
        this.parent = parent;
    }

    public static boolean isValidField(Field f, Object rootObj) {
        return (f.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) == 0 &&
            f.canAccess(rootObj);
    }

    @Override
    protected void init() {
        int x = PADDING;
        int y = PADDING;
        for(Field field : List.of(this.configClass.getFields())) {
            if(!isValidField(field, option)) continue;
            try {
                FieldWidget<?,?> widget = FieldWidget.create(field,x,y,WIDGET_WIDTH,WIDGET_HEIGHT,field.get(option));
                widgets.add(widget);
                this.addDrawableChild(widget.getWidget());
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
}
