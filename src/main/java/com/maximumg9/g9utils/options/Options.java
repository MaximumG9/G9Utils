package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.DontShowInGUI;
import com.maximumg9.g9utils.config.Name;

@SuppressWarnings("CanBeFinal")
public class Options implements com.maximumg9.g9utils.config.Options {
    public static final Integer MOD_CONFIG_VERSION = 1;

    @DontShowInGUI
    public Integer CONFIG_VERSION = MOD_CONFIG_VERSION;

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
    @Name("Notify On Lagback")
    public Boolean seeLagBack = false;
    @Name("Predict Pearl Landing (approx.)")
    public Boolean seeExpectedRandomlessPearl = false;
    @Name("Make tweakeroo not leak itself")
    public Boolean stopTweakerooLeak = false;

    @Name("Dragon Hitboxes")
    public DragonOptions dragonOptions = new DragonOptions();
}
