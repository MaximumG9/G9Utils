package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.Config;
import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Options;

@SuppressWarnings("CanBeFinal")
public class TechnicallyCheatOptions implements Options {
    public static Config<TechnicallyCheatOptions> create() {
        return new Config<>(null, TechnicallyCheatOptions::new);
    }

    @Name("Deceleration Enabled")
    public Boolean deceleration = true;
    @Name("Don't Stop Sprinting")
    public Boolean dontStopSprinting = false;
    @Name("Crawl Walking Into Block (!)")
    public Boolean autoCrawl = false;
    @Name("Insta-mine same block (!)")
    public Boolean instaMineSameBlock = false;
    @Name("Prioritize Offhand if Stripping Logs")
    public Boolean dontStripWithItemInOffhand = false;
    @Name("Prioritize Offhand")
    public Boolean prioritizeOffhand = false;
}
