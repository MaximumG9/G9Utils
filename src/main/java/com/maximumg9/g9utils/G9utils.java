package com.maximumg9.g9utils;

import com.google.common.collect.ImmutableSet;
import com.maximumg9.g9utils.config.Config;
import com.maximumg9.g9utils.options.Options;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

public class G9utils implements ModInitializer {
    private static Config<Options> config;
    private static final File CONFIG_FILE = new File("config/g9utils-options.json");
    private static final File OLD_CONFIG_FILE = new File("config/g9utils-options-old.json");
    private static final Logger LOGGER = LogUtils.getLogger();

    public static SwordHitType lastSwordHitType = null;

    public static int timeSinceLastAttack = 0;
    public static int timeSinceLastSwap = 0;
    public static ItemStack possibleAttributeSwapStack = null;
    public static AttributeSwap lastAttributeSwap = null;

    public static ImmutableSet<String> VANILLA_LANGUAGE_KEYS;

    public static HashSet<LivingEntity> queuedLivingTagsUpdate = new HashSet<>();
    public static boolean runningQueuedTagUpdate = false;

    public static Identifier id(String path) {
        return Identifier.of("g9utils",path);
    }

    @Override
    public void onInitialize() {

    }

    public static void onMCInitEnd() {
        config = new Config<>(CONFIG_FILE, Options::new, Options.class);
        LOGGER.info("Initializing G9Utils");
        try {
            config.loadConfig();
        } catch (IOException e) {
            // if the config file doesn't exist / is malformed save a default config file
            LOGGER.warn("Failed to read config file (likely missing or corrupted)", e);
            config.setToDefault();
            forceSaveConfig();
        }
        if(config.opt() == null) {
            // sometimes with updates and stuff the config can be null
            LOGGER.warn("Config file was null, overwriting");
            config.setToDefault();
        } else if(config.opt().CONFIG_VERSION > Options.MOD_CONFIG_VERSION) {
            LOGGER.warn("Config file was of config version {} but g9utils is of config version {}", config.opt().CONFIG_VERSION, Options.MOD_CONFIG_VERSION);
            LOGGER.warn("Moving previous config to {} ", OLD_CONFIG_FILE);
            if(CONFIG_FILE.renameTo(OLD_CONFIG_FILE)) {
                LOGGER.warn("Successfully moved outdated config file to {}, writing new default config", OLD_CONFIG_FILE);
            } else {
                throw new IllegalStateException("Failed to back up outdated G9Utils config, stopping game");
            }
            config.setToDefault();
        }
        if(!Objects.equals(config.opt().CONFIG_VERSION, Options.MOD_CONFIG_VERSION)) {

        }
        if(config.opt() == null) {
            LOGGER.error("Default config is null");
        }
        forceSaveConfig();

        LOGGER.info("Done initializing G9Utils");
    }

    private static void forceSaveConfig() {
        try {
            config.saveConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to save config file, we're screwed", e);
            throw new RuntimeException(e);
        }
    }

    public static void handleInputs(MinecraftClient client) {
        assert client.player != null;
        while(G9utils.opt().cheats.swingMainHandBind.wasPressed()) {
            client.player.swingHand(Hand.MAIN_HAND);
        }
        while(G9utils.opt().cheats.swingOffHandBind.wasPressed()) {
            client.player.swingHand(Hand.OFF_HAND);
        }
    }

    public static void tick() {
        if(timeSinceLastSwap >= 0) {
            timeSinceLastSwap++;
        }
        if(timeSinceLastAttack >= 0) {
            timeSinceLastAttack++;
        }
    }

    public static Options opt() {
        return config.opt();
    }

    public static Config<Options> getConfig() {
        return config;
    }
}
