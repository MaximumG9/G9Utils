package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.Config;
import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Range;

public class Options implements com.maximumg9.g9utils.config.Options {
    @Name("HUD")
    public Config<HUDOptions> hudOptions = HUDOptions.create();

    @Name("No Fire Overlay When Resistant")
    public Boolean NoFireWhenResistant = false;
    @Name("Crawl Walking Into Block (!)")
    public Boolean autoCrawl = false;

    @Name("Prioritize Offhand if Stripping Logs")
    public Boolean dontStripWithItemInOffhand = false;
    @Name("Prioritize Offhand")
    public Boolean prioritizeOffhand = false;
    @Name("Add Random Digits to F3")
    public Boolean addRandomDigitsToF3 = false;
    @Name("Deceleration Enabled")
    public Boolean deceleration = true;
    @Name("Don't Stop Sprinting")
    public Boolean dontStopSprinting = false;
    @Name("Notify On Lagback")
    public Boolean seeLagBack = false;
    @Name("See Lag Affected Self (approx.)")
    public Boolean seeLagAffectedSelf = false;
    @Name("Insta-mine same block (!)")
    public Boolean instaMineSameBlock = false;

    @Name("Dragon Hitboxes")
    public Config<DragonOptions> dragonOptions = DragonOptions.create();

    @Name("Lag in ms")
    @Range(max=500)
    public Integer lag = 50;

    @Name("Number of Random Digits ")
    @Range(max = 100)
    public Integer numRandomDigits = 10;
}
