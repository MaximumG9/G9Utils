package com.maximumg9.g9utils.config;

import com.maximumg9.g9utils.Util;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class TypedField<T,O> {
    private final Field backingField;
    public TypedField(Field field, Class<T> valueType, Class<O> declaringType) {
        this.backingField = field;
        if(this.backingField.getType().isPrimitive()) {
            throw new IllegalArgumentException("Backing field for a TypedField cannot be a primitive");
        }
        if(this.backingField.getType() != valueType) {
            throw new IllegalArgumentException("valueType is not the runtime type of the field given");
        }
        if(this.backingField.getDeclaringClass() != declaringType) {
            throw new IllegalArgumentException("valueType is not the owner type of the field given");
        }
    }

    public String getName() {
        return this.backingField.getName();
    }

    public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationClass) {
        return this.backingField.getAnnotation(annotationClass);
    }

    public <A extends java.lang.annotation.Annotation> boolean isAnnotationPresent(Class<A> annotationClass) {
        return this.backingField.isAnnotationPresent(annotationClass);
    }

    public void setAccessible(boolean accessibility) {
        this.backingField.setAccessible(accessibility);
    }

    public int getModifiers() {
        return this.backingField.getModifiers();
    }

    ///  USES THE ACCESSIBILITY THAT TypedField HAS NOT THAT THE TRUE CALLER HASSSSSS
    public boolean canAccess(Object rootObj) {
        return this.backingField.canAccess(rootObj);
    }

    // The suppressed warnings here are ones guaranteed by the semantics of the normal field object
    @SuppressWarnings("unchecked")
    public Class<T> getValueType() {
        return (Class<T>) this.backingField.getType();
    }

    @SuppressWarnings("unchecked")
    public Class<O> getDeclaringType() {
        return (Class<O>) this.backingField.getDeclaringClass();
    }

    @SuppressWarnings("unchecked")
    public <V extends O> T get(V owner) throws IllegalAccessException {
        return (T) backingField.get(owner);
    }

    public <V extends O> void set(V owner, T value) throws IllegalAccessException {
        backingField.set(owner,value);
    }

    @SuppressWarnings("unchecked")
    public <V> TypedField<V,O> deduce(Class<V> valueTestClass) {
        if(valueTestClass.isAssignableFrom(this.getValueType())) {
            return (TypedField<V, O>) this;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <O> Stream<TypedField<?,O>> getDeclaredFields(Class<O> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .map(
                (field) ->
                    // (Cursed and broken bullshit)
                    new TypedField<>(field, (Class<Object>) field.getType(), clazz)
            );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <O> Stream<TypedField<?,O>> getAllDeclaredFields(Class<O> clazz) {
        return (Stream<TypedField<?,O>>) Stream.iterate((Class) clazz,
            Objects::nonNull,
            Class::getSuperclass
        ).flatMap(
            (c) -> getDeclaredFields(c)
        );
    }

    public static <O> Stream<TypedField<?,O>> getAllFields(Class<O> clazz) {
        return Arrays.stream(clazz.getFields()).map(
            (field) ->
            new TypedField<>(field,field.getType(),clazz)
        );
    }

    // Additional requirements for O,
    // object given must be exactly of type O and not some subclass
    public static <T,O> List<TypedField<T,O>> getAllFieldsRecursive(O opt, Class<T> searchClass) {
        Class<O> optClass = Util.getClassStrict(opt);
        ArrayList<TypedField<T,O>> list = new ArrayList<>();
        getAllDeclaredFields(optClass).forEach(
            (field) -> {
                try {
                    TypedField<T,O> possibleDeduction = field.deduce(searchClass);
                    if(possibleDeduction != null) {
                        list.add(possibleDeduction);
                    } else {
                        TypedField<O, O> possibleRecursion = field.deduce(optClass);
                        if (possibleRecursion != null) {
                            list.addAll(
                                getAllFieldsRecursive(
                                    possibleRecursion.get(opt),
                                    searchClass
                                )
                            );
                        }
                    }
                } catch (IllegalAccessException ignored) {}
            }
        );
        return list;
    }

    public static <T,O> List<T> getAllValuesRecursive(O opt, Class<T> searchClass) {
        return getAllFieldsRecursive(opt,searchClass).stream()
            .map((field) -> {
                try {
                    return field.get(opt);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
    }
}
