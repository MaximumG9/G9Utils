package com.maximumg9.g9utils.config;

import net.minecraft.client.gui.screen.Screen;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ConfigScreen<O extends Options> extends OptionsScreen<O> {
    private final Config<O> conf;

    public ConfigScreen(Screen parent, Config<O> conf) {
        super(parent,conf.opt());
        this.conf = conf;
    }

    public static boolean isValidField(Field f, Object rootObj) {
        return (f.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) == 0 &&
            f.canAccess(rootObj);
    }

    @Override
    public void close() {
        super.close();
        try {
            this.conf.saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
