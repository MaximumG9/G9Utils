package com.maximumg9.g9utils.config;

import com.maximumg9.g9utils.Util;
import net.minecraft.client.MinecraftClient;
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

    @SuppressWarnings("unchecked")
    public static <V> FieldWidget<?,V> create(Field field, int x, int y, int width, int height, V currentValue) {
        Name possibleName = field.getAnnotation(Name.class);
        String name;
        if(possibleName == null) {
            name = field.getName();
        } else {
            name = possibleName.value();
        }

        if(field.getType().equals(Boolean.class)) {
            // The type checker doesn't understand this, but this must mean that V is a Boolean

            var widget = CyclingButtonWidget.builder(
                (O) -> Text.of(O.toString()),
                (Boolean) currentValue
            )
                .values(true,false)
                .build(
                    x,
                    y,
                    width,
                    height,
                    Text.of(name)
                );

            return new FieldWidget<>(field, widget, (w) -> (V) w.getValue());
        } else if(Util.getClassStrict(currentValue).equals(Integer.class)) {
            Range possibleRange = field.getAnnotation(Range.class);

            if(possibleRange != null) {
                int min = (int)possibleRange.min();
                int max = (int)possibleRange.max();

                var widget = new SliderWidget(
                    x,y,
                    width,height,
                    Text.of(name + ": " + currentValue),
                    ((double)((int) currentValue - min)/(max-min))
                    ) {
                    @Override
                    protected void updateMessage() {
                        this.setMessage(Text.of(name + ": " + internalValue));
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
            } else {
                throw new IllegalArgumentException("Number field " + field + " doesn't have a valid range");
            }
        } else if(Util.getClassStrict(currentValue).equals(Float.class)) {
            Range possibleRange = field.getAnnotation(Range.class);

            if(possibleRange != null) {
                float min = (float) possibleRange.min();
                float max = (float) possibleRange.max();

                var widget = new SliderWidget(
                    x,y,
                    width,height,
                    Text.of(name + ": " + String.format("%.1f",(float) currentValue)),
                    ((double)((float) currentValue - min)/(max-min))
                ) {
                    @Override
                    protected void updateMessage() {
                        this.setMessage(
                            Text.of(
                                name + ": " +
                                    String.format("%.1f",internalValue)
                            )
                        );
                        this.setTooltip(
                            Tooltip.of(
                                Text.of(
                                    String.format("%.1f",internalValue)
                                )
                            )
                        );
                    }

                    float internalValue = (float) currentValue;

                    public float getValue() {
                        return internalValue;
                    }

                    @Override
                    protected void applyValue() {
                        internalValue = (float) ((this.value * (max - min)) + min);
                    }
                };

                return new FieldWidget<>(field, widget, (w) -> (V) ((Float) w.getValue()));
            } else {
                throw new IllegalArgumentException("Number field " + field + " doesn't have a valid range");
            }
        } else if(currentValue instanceof Config<?> conf) {
            return (FieldWidget<?, V>) getConfigButton(
                conf,field,
                name,
                x,y,
                width,height
            );
        }
        throw new IllegalArgumentException("NUH UH! THAT'S NOT A VALID TYPE YOU!!!!");
    }

    private static <O extends Options>
    FieldWidget<ConfigButton<O>,Config<O>>
    getConfigButton(
        Config<O> config,
        Field field,
        String name,
        int x, int y,
        int width, int height
    ) {
        var widget = new ConfigButton.Builder<>(
            Text.literal(name),
            config,
            MinecraftClient.getInstance().textRenderer
        ).dimensions(x, y, width, height).build();

        return new FieldWidget<>(
            field,
            widget,
            (w) ->
                w.config
        );
    }
}
