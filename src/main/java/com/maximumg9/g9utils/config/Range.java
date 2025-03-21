package com.maximumg9.g9utils.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Range {
    double min() default 0;
    double max() default 100;
}
