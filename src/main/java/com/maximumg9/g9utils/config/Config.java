package com.maximumg9.g9utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maximumg9.g9utils.Util;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class Config<O extends Options> {
    private transient final Class<O> optionClass;
    @Nullable private transient final File configFile;
    private O options;
    private final Gson gson;

    public Config(@Nullable File configFile, OptionsFactory<O> factory) {
        this.options = factory.create();
        this.optionClass = Util.getClassStrict(this.options);
        this.configFile = configFile;
        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();
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
