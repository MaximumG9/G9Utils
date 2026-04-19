package com.maximumg9.g9utils.config.gui;

import com.maximumg9.g9utils.config.TypedField;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class IntSliderWidget extends SliderWidget {
    private int internalValue;
    private final String name;

    private final int min;
    private final int max;

    private IntSliderWidget(int x, int y, int width, int height, String name, int value, int min, int max) {
        super(x,y,
            width,height,
            getText(name,value),
            ((double)(value - min)/(max-min)));
        this.name = name;
        this.internalValue = value;
        this.min = min;
        this.max = max;
    }

    public static <O> FieldWidget.FieldWidgetBuilder<IntSliderWidget,Integer,O> builder(TypedField<Integer,O> field, int currentValue, int min, int max, String name) {
        return new FieldWidget.FieldWidgetBuilder<>(
            (x,y,width,height,name1) ->
                new IntSliderWidget(x,y,width,height,name1,currentValue,min,max),
            IntSliderWidget::getValue,
            field,
            name
        );
    }

    private static Text getText(String name, int value) {
        return Text.of(name + ": " + value);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(getText(name,internalValue));
        this.setTooltip(Tooltip.of(Text.of(String.valueOf(internalValue))));
    }

    public int getValue() {
        return internalValue;
    }

    @Override
    protected void applyValue() {
        internalValue = (int) (this.value * (max - min)) + min;
    }
}
