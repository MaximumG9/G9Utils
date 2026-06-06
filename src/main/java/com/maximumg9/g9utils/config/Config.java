package com.maximumg9.g9utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class Config<O extends Options> {
    private transient final Class<O> optionClass;
    private transient final OptionsFactory<O> optionsFactory;
    @Nullable private transient final File configFile;
    private O options;
    private static final Gson gson;
    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(
            Keybind.class,
            new BindingTypeAdapters.BindingSerializer()
        );
        builder.registerTypeAdapter(
            Keybind.class,
            new BindingTypeAdapters.BindingDeserializer()
        );
        gson = builder.create();
    }

    public Config(@Nullable File configFile, OptionsFactory<O> factory, Class<O> optionClass) {
        this.optionsFactory = factory;
        this.optionClass = optionClass;
        this.configFile = configFile;
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

    public void setToDefault() {
        this.options = optionsFactory.create();
    }

    public void loadConfig() throws IOException {
        if(configFile == null) return;
        Reader reader = new FileReader(configFile);
        this.options = gson.fromJson(reader, optionClass);
        reader.close();
    }
}
