package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.Config;
import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Range;

@SuppressWarnings("CanBeFinal")
public class Options implements com.maximumg9.g9utils.config.Options {
    @Name("HUD")
    public Config<HUDOptions> hudOptions = HUDOptions.create();

    @Name("Technically Cheats")
    public Config<TechnicallyCheatOptions> cheats = TechnicallyCheatOptions.create();

    @Name("No Fire Overlay When Resistant")
    public Boolean NoFireWhenResistant = false;
    @Name("Add Random Digits to F3")
    public Boolean addRandomDigitsToF3 = false;
    @Name("Notify On Lagback")
    public Boolean seeLagBack = false;
    @Name("See Lag Affected Self (approx.)")
    public Boolean seeLagAffectedSelf = false;

    @Name("Dragon Hitboxes")
    public Config<DragonOptions> dragonOptions = DragonOptions.create();

    @Name("Lag in ms")
    @Range(max=500)
    public Integer lag = 50;

    @Name("Number of Random Digits ")
    @Range()
    public Integer numRandomDigits = 10;
}
