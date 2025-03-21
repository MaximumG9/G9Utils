package com.maximumg9.g9utils;

public class Util {
    public static <O> Class<O> getClassStrict(O object) {
        return (Class<O>) object.getClass();
    }
}
