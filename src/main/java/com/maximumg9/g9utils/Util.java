package com.maximumg9.g9utils;

public class Util {
    @SuppressWarnings("unchecked")
    public static <O> Class<O> getClassStrict(O object) {
        return (Class<O>) object.getClass();
    }
}
