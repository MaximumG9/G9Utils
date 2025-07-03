package com.maximumg9.g9utils.config;

import com.maximumg9.g9utils.Util;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.function.Function;

public class FieldWidget<W extends Element & Drawable & Selectable,V> {
    private final Field field;
    private final W widget;
    private final Function<W,V> widgetToValueFunction;

    private FieldWidget(Field field, W widget, Function<W,V> widgetToValueFunction) {
        field.setAccessible(true);
        this.field = field;
        this.widgetToValueFunction = widgetToValueFunction;
        this.widget = widget;
    }

    public W getWidget() {
        return widget;
    }

    public void save(Object fieldOwner) throws IllegalAccessException {
        field.set(fieldOwner,widgetToValueFunction.apply(this.widget));
    }

    public static <V> FieldWidget<?,V> create(Field field,int x,int y,int width,int height,V currentValue) {
        Name possibleName = field.getAnnotation(Name.class);
        String name;
        if(possibleName == null) {
            name = field.getName();
        } else {
            name = possibleName.value();
        }

        if(field.getType().equals(Boolean.class)) {
            // The type checker doesn't understand this, but this must mean that V is a Boolean

            var widget = new CyclingButtonWidget.Builder<>((O) -> Text.of(O.toString()))
                    .values(true,false)
                    .initially(currentValue)
                    .build(
                        x,
                        y,
                        width,
                        height,
                        Text.of(name + ": ")
                    );

            return new FieldWidget<>(field, widget, (w) -> (V) w.getValue());
        }
        if(Util.getClassStrict(currentValue).equals(Integer.class)) {
            Range possibleRange = field.getAnnotation(Range.class);

            if(possibleRange != null) {
                int min = (int)possibleRange.min();
                int max = (int)possibleRange.max();

                String finalName = name;
                var widget = new SliderWidget(
                    x,y,
                    width,height,
                    Text.of(finalName + ": " + currentValue),
                    ((double)((int) currentValue - min)/(max-min))
                    ) {
                    @Override
                    protected void updateMessage() {
                        this.setMessage(Text.of(finalName + ": " + internalValue));
                        this.setTooltip(Tooltip.of(Text.of(String.valueOf(internalValue))));
                    }

                    int internalValue = (int) currentValue;

                    public int getValue() {
                        return internalValue;
                    }

                    @Override
                    protected void applyValue() {
                        internalValue = (int) (this.value * (max - min)) + min;
                    }
                };

                return new FieldWidget<>(field, widget, (w) -> (V) ((Integer) w.getValue()));
            }
        }
        throw new IllegalArgumentException("NUH UH! THAT'S NOT A VALID TYPE YOU!!!!");
    }
}
