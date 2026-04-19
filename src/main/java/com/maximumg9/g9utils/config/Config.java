package com.maximumg9.g9utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maximumg9.g9utils.Util;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;

public class Config<O extends Options> {
    private transient final Class<O> optionClass;
    @Nullable private transient final File configFile;
    private O options;
    private final Gson gson;
    private final BindingTypeAdapters.BindingDeserializer<O> bindingDeserializer;

    public Config(@Nullable File configFile, OptionsFactory<O> factory) {
        this.options = factory.create();
        this.optionClass = Util.getClassStrict(this.options);
        this.configFile = configFile;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(
            KeyBinding.class,
            new BindingTypeAdapters.BindingSerializer()
        );
        this.bindingDeserializer = new BindingTypeAdapters.BindingDeserializer<>(this.options);
        builder.registerTypeAdapter(
            KeyBinding.class,
            bindingDeserializer
        );
        this.gson = builder.create();
    }

    public HashMap<InputUtil.Key, KeyBinding> getAllKeybinds() {
        return this.bindingDeserializer.getCustomBindings();
    }

    public void saveConfig() throws IOException {
        if(configFile == null) return;
        Writer writer = new FileWriter(configFile);
        writer.write(gson.toJson(this.options, this.optionClass));
        writer.close();
    }

    public O opt() {
        return this.options;
    }

    public void loadConfig() throws IOException {
        if(configFile == null) return;
        Reader reader = new FileReader(configFile);
        this.options = gson.fromJson(reader, optionClass);
        reader.close();
    }
}
