package com.maximumg9.g9utils;

import com.maximumg9.g9utils.config.Range;

public class Options implements com.maximumg9.g9utils.config.Options {
    public Boolean NoFireWhenResistant = false;
    public Boolean autoCrawl = false;
    public Boolean seeCosAndSinForYaw = false;
    public Boolean seeRadianRoundedYaw = false;
    public Boolean seeAccurateYaw = false;

    @Range(max = 10)
    public Integer yawDecimalPlaces = 6;
}
