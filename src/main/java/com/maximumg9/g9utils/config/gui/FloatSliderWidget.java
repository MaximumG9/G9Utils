package com.maximumg9.g9utils.config.gui;

import com.maximumg9.g9utils.config.TypedField;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class FloatSliderWidget extends SliderWidget {
    private float internalValue;
    private final String name;
    private final float min;
    private final float max;

    public FloatSliderWidget(int x,
                             int y,
                             int width,
                             int height,
                             String name,
                             float currentValue,
                             float min,
                             float max
    ) {
        super(
            x,y,
            width,height,
            Text.of(name + ": " + String.format("%.1f",currentValue)),
            ((double)(currentValue - min)/(max-min))
        );
        internalValue = currentValue;
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public static <O> FieldWidget.FieldWidgetBuilder<FloatSliderWidget,Float,O> builder(
        TypedField<Float,O> field,
        float currentValue,
        float min,
        float max,
        String name
    ) {
        return new FieldWidget.FieldWidgetBuilder<>(
            (x,y,width,height,name1) ->
                new FloatSliderWidget(x,y,width,height,name1,currentValue,min,max),
            FloatSliderWidget::getValue,
            field,
            name
        );
    }

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

    public float getValue() {
        return internalValue;
    }

    @Override
    protected void applyValue() {
        internalValue = (float) ((this.value * (max - min)) + min);
    }
}