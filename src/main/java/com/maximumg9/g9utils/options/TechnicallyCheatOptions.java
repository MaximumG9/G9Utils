package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.Keybind;
import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Options;
import com.maximumg9.g9utils.config.Range;
import net.minecraft.client.util.InputUtil;

@SuppressWarnings("CanBeFinal")
public class TechnicallyCheatOptions implements Options {
    @Name("Deceleration Enabled (!)")
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
    @Name("Quake-esque Air Movement (!)")
    public Boolean quakeAir = false;
    @Range(min=0.0,max=20.0)
    @Name("sv_air_accelerate")
    public Float airAccelerate = 5.0f;
    @Name("Remove Jump Cooldown")
    public Boolean constantJump = false;

    @Name("Test keybind")
    public Keybind testKeyBind = Keybind.fromKeycode(InputUtil.GLFW_KEY_R);
}
