package com.maximumg9.g9utils;

import com.maximumg9.g9utils.config.Config;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class G9utils implements ModInitializer {
    private static Config<Options> config;
    private static final File FILE = new File("g9utils-options.json");
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        config = new Config<>(FILE, Options::new);
        LOGGER.info("Initializing G9Utils");
        try {
            config.loadConfig();
        } catch (IOException e) {
            // if the config file doesn't exist / is malformed save a default config file
            LOGGER.warn("Failed to read config file, overwriting", e);
            try {
                config = new Config<>(FILE, Options::new);
                config.saveConfig();
            } catch (IOException ex) {
                LOGGER.error("Failed to save config file, we're screwed", ex);
                throw new RuntimeException(ex);
            }
        }
        try {
            config.saveConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to save config file, we're screwed", e);
            throw new RuntimeException(e);
        }
        LOGGER.info("Done Initializing G9Utils");
    }
    

    public static Options getOptions() {
        return config.getOptions();
    }

    public static Config<Options> getConfig() {
        return config;
    }
}
