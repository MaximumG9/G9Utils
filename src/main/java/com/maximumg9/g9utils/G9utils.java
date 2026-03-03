package com.maximumg9.g9utils;

import com.maximumg9.g9utils.config.Config;
import com.maximumg9.g9utils.options.Options;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class G9utils implements ModInitializer {
    private static Config<Options> config;
    private static final File FILE = new File("g9utils-options.json");
    private static final Logger LOGGER = LogUtils.getLogger();

    public static SwordHitType lastSwordHitType = null;

    @Override
    public void onInitialize() {
        config = new Config<>(FILE, Options::new);
        LOGGER.info("Initializing G9Utils");
        try {
            config.loadConfig();
        } catch (IOException e) {
            // if the config file doesn't exist / is malformed save a default config file
            LOGGER.warn("Failed to read config file, overwriting", e);
            config = new Config<>(FILE, Options::new);
            forceSaveConfig();
        }
        if(config.opt() == null) {
            // sometimes with updates and stuff the config can be null
            LOGGER.warn("Config file was null, overwriting");
            config = new Config<>(FILE, Options::new);
            forceSaveConfig();
        }
        forceSaveConfig();
        LOGGER.info("Done Initializing G9Utils");
    }

    private static void forceSaveConfig() {
        try {
            config.saveConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to save config file, we're screwed", e);
            throw new RuntimeException(e);
        }
    }

    public static Options opt() {
        return config.opt();
    }

    public static Config<Options> getConfig() {
        return config;
    }
}
