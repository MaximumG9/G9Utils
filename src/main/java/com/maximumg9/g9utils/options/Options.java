package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Range;

@SuppressWarnings("CanBeFinal")
public class Options implements com.maximumg9.g9utils.config.Options {
    @Name("HUD")
    public HUDOptions hudOptions = new HUDOptions();

    @Name("Technically Cheats")
    public TechnicallyCheatOptions cheats = new TechnicallyCheatOptions();

    @Name("Useless Options")
    public UselessOptions useless = new UselessOptions();

    @Name("Rendering Options")
    public RenderingOptions rendering = new RenderingOptions();

    @Name("No Fire Overlay When Resistant")
    public Boolean NoFireWhenResistant = false;
    @Name("Add Random Digits to F3")
    public Boolean addRandomDigitsToF3 = false;
    @Name("Notify On Lagback")
    public Boolean seeLagBack = false;
    @Name("See Lag Affected Self (approx.)")
    public Boolean seeLagAffectedSelf = false;
    @Name("Predict Pearl Landing (approx.)")
    public Boolean seeExpectedRandomlessPearl = false;
    @Name("Make tweakeroo not leak itself")
    public Boolean stopTweakerooLeak = false;

    @Name("Dragon Hitboxes")
    public DragonOptions dragonOptions = new DragonOptions();

    @Name("Lag in ms")
    @Range(max=500)
    public Integer lag = 50;

    @Name("Number of Random Digits ")
    @Range()
    public Integer numRandomDigits = 10;
}
