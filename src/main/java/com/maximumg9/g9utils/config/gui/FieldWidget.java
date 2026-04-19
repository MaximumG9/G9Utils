package com.maximumg9.g9utils.config.gui;

import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Options;
import com.maximumg9.g9utils.config.Range;
import com.maximumg9.g9utils.config.TypedField;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.util.function.Function;

public class FieldWidget<W extends Element & Drawable & Selectable,V,O> {
    private final TypedField<V,O> field;
    private final W widget;
    private final Function<W,V> widgetToValueFunction;

    private FieldWidget(TypedField<V,O> field, W widget, Function<W,V> widgetToValueFunction) {
        field.setAccessible(true);
        this.field = field;
        this.widgetToValueFunction = widgetToValueFunction;
        this.widget = widget;
    }

    public W getWidget() {
        return widget;
    }

    public void save(O fieldOwner) throws IllegalAccessException {
        field.set(fieldOwner,widgetToValueFunction.apply(this.widget));
    }

    public static <V,O> FieldWidgetBuilder<?,V,O> createBuilder(TypedField<V,O> field, O owner) throws IllegalAccessException {
        return createBuilderWithValue(field,field.get(owner));
    }

    public static <V,O> FieldWidgetBuilder<?,V,O> createBuilderWithValue(TypedField<V,O> field, V currentValue) {
        Name possibleName = field.getAnnotation(Name.class);
        String name;
        if(possibleName == null) {
            name = field.getName();
        } else {
            name = possibleName.value();
        }

        if(currentValue instanceof Boolean bValue) {
            Convertibility<V,Boolean> c = Convertibility
                .classEquality(field.getValueType(),Boolean.class);
            return c.cA(new FieldWidgetBuilder<>(
                (x,y,width,height, name1) ->
                    CyclingButtonWidget.builder(
                        (b) -> Text.of(b.toString()),
                        bValue
                    ).values(true,false)
                    .build(
                        x,
                        y,
                        width,
                        height,
                        Text.of(name1)
                    ),
                CyclingButtonWidget::getValue,
                field.deduce(Boolean.class),
                name
            ));
        } else if(currentValue instanceof Integer iValue) {
            Convertibility<V,Integer> c = Convertibility
                .classEquality(field.getValueType(),Integer.class);
            Range possibleRange = field.getAnnotation(Range.class);
            if(possibleRange != null) {
                int min = (int)possibleRange.min();
                int max = (int)possibleRange.max();
                return c.cA(IntSliderWidget.builder(
                    field.deduce(Integer.class),
                    iValue,
                    min,max,
                    name
                ));
            } else {
                throw new IllegalArgumentException("Number field " + field + " doesn't have a valid range");
            }
        } else if(currentValue instanceof Float fValue) {
            Convertibility<V,Float> c = Convertibility.classEquality(
                field.getValueType(),
                Float.class
            );
            Range possibleRange = field.getAnnotation(Range.class);
            if(possibleRange != null) {
                float min = (float) possibleRange.min();
                float max = (float) possibleRange.max();
                return c.cA(FloatSliderWidget.builder(
                    field.deduce(Float.class),
                    fValue,
                    min,max,
                    name
                ));
            } else {
                throw new IllegalArgumentException("Number field " + field + " doesn't have a valid range");
            }
        } else if(currentValue instanceof Options opts) {
            // Inexactness (currentValue.getClass() != Options.class) means
            // Convertibility and TypedField.deduce can't be used

            //noinspection unchecked
            return (FieldWidgetBuilder<?, V, O>)
                    ConfigButton.builder(
                        field.deduce(Options.class),
                        opts,
                        name
                    );
        } else if(currentValue instanceof KeyBinding binding) {

        }
        throw new IllegalArgumentException("NUH UH! THAT'S NOT A VALID TYPE YOU!!!!");
    }

    public static class FieldWidgetBuilder<W extends Element & Drawable & Selectable,V,O> {
        private final WidgetFactory<W> factory;
        private int x;
        private int y;
        private int width;
        private int height;
        private final String name;
        private final Function<W,V> wvFunction;
        private final TypedField<V,O> field;
        public FieldWidgetBuilder(
            WidgetFactory<W> factory,
            Function<W,V> widgetToValueFunction,
            TypedField<V,O> field,
            String name
        ) {
            this.factory = factory;
            this.wvFunction = widgetToValueFunction;
            this.name = name;
            this.field = field;
        }

        public FieldWidgetBuilder<W,V,O> dimensions(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        public FieldWidget<W,V,O> buildFieldWidget() {
            return new FieldWidget<>(
                this.field,
                factory.build(x,y,width,height,name),
                this.wvFunction
            );
        }

        @FunctionalInterface
        public interface WidgetFactory<W extends Element & Drawable & Selectable> {
            W build(int x, int y, int width, int height, String nameSupplier);
        }
    }
}
