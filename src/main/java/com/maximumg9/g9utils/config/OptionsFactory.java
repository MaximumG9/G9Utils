package com.maximumg9.g9utils.config;

public interface OptionsFactory<O extends Options> {
    O create();
}
