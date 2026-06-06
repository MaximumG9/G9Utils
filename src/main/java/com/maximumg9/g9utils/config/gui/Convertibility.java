package com.maximumg9.g9utils.config.gui;


import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;

// Represents that A and B are the same class
public class Convertibility<A,B> {

    private Convertibility() {
    }

    @SuppressWarnings("unchecked")
    public B cB(A value) {
        return (B) value;
    }

    @SuppressWarnings("unchecked")
    public A cA(B value) {
        return (A) value;
    }

    // I love making niche specific functions for specific cases :D
    @SuppressWarnings("unchecked")
    public <W extends Element & Drawable & Selectable,O>
    FieldWidget.FieldWidgetBuilder<W,A,O> cA(FieldWidget.FieldWidgetBuilder<W,B,O> builder) {
        return (FieldWidget.FieldWidgetBuilder<W,A,O>) builder;
    }

    @SuppressWarnings("unchecked")
    public <W extends Element & Drawable & Selectable,O>
    FieldWidget.FieldWidgetBuilder<W,B,O> cB(FieldWidget.FieldWidgetBuilder<W,A,O> builder) {
        return (FieldWidget.FieldWidgetBuilder<W,B,O>) builder;
    }

    public static <A,B> Convertibility<A,B> classEquality(Class<A> aClass, Class<B> bClass) {
        if(aClass == bClass) {
            return new Convertibility<>();
        } else {
            throw new IllegalStateException("Inconvertible");
        }
    }
}
