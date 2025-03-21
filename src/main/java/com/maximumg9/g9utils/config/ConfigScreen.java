package com.maximumg9.g9utils.config;

import com.maximumg9.g9utils.Util;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigScreen<O extends Options> extends Screen {
    private static final int WIDGET_WIDTH = 200;
    private static final int WIDGET_HEIGHT = 20;
    private static final int PADDING = 10;

    private final Config<O> config;
    private final Class<O> configClass;
    private final List<FieldWidget<?,?>> widgets = new ArrayList<>();

    public ConfigScreen(Config<O> config) {
        super(Text.of(config.getClass().getSimpleName()));
        this.configClass = Util.getClassStrict(config.getOptions());
        this.config = config;
    }

    @Override
    protected void init() {
        int x = PADDING;
        int y = PADDING;
        for(Field field : List.of(this.configClass.getFields())) {
            try {
                FieldWidget<?,?> widget = FieldWidget.create(field,x,y,WIDGET_WIDTH,WIDGET_HEIGHT,field.get(config.getOptions()));
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
                widget.save(config.getOptions());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            config.saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.close();
    }
}
