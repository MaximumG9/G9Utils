package com.maximumg9.g9utils.config;

import com.google.gson.Gson;

import java.io.*;

public class Config<O extends Options> {
    private final Class<O> optionClass;
    private final File configFile;
    private O options;

    public Config(File configFile, OptionsFactory<O> factory) {
        this.options = factory.create();
        this.optionClass = (Class<O>) this.options.getClass();
        this.configFile = configFile;
    }

    public void saveConfig() throws IOException {
        Writer writer = new FileWriter(configFile);
        Gson gson = new Gson();
        writer.write(gson.toJson(this.options, this.optionClass));
        writer.close();
    }

    public O getOptions() {
        return this.options;

    }

    public void loadConfig() throws IOException {
        Reader reader = new FileReader(configFile);
        Gson gson = new Gson();
        this.options = gson.fromJson(reader, optionClass);
        reader.close();
    }
}
