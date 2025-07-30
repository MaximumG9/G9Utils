package com.maximumg9.g9utils;

import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Range;

public class Options implements com.maximumg9.g9utils.config.Options {
    @Name("No Fire Overlay When Resistant")
    public Boolean NoFireWhenResistant = false;
    @Name("Crawl Walking Into Block (!)")
    public Boolean autoCrawl = false;
    @Name("See Cos and Sin of Yaw")
    public Boolean seeCosAndSinForYaw = false;
    @Name("See Yaw in Radians")
    public Boolean seeRadianRoundedYaw = false;
    @Name("See Unclamped Yaw")
    public Boolean seeAccurateYaw = false;
    @Name("Prioritize Offhand if Stripping Logs")
    public Boolean dontStripWithItemInOffhand = false;
    @Name("Prioritize Offhand")
    public Boolean prioritizeOffhand = false;
    @Name("Add Random Digits to F3")
    public Boolean addRandomDigitsToF3 = false;
    @Name("Deceleration Enabled")
    public Boolean deceleration = true;
    @Name("See onGround")
    public Boolean seeOnGround = true;
    @Name("See Pos")
    public Boolean seePos = false;
    @Name("Don't Stop Sprinting")
    public Boolean dontStopSprinting = false;
    @Name("See Velocity")
    public Boolean seeVel = false;
    @Name("Notify On Lagback")
    public Boolean seeLagBack = false;
    @Name("See Sword Hit Type")
    public Boolean seeSwordHitType = false;
    @Name("See Server Side Sprint (approx.)")
    public Boolean seeServerSideSprint = false;
    @Name("See Lag Affected Self (approx.)")
    public Boolean seeLagAffectedSelf = false;
    @Name("Insta-mine same block (!)")
    public Boolean instaMineSameBlock = false;

    @Name("Lag in ms")
    @Range(max=500)
    public Integer lag = 50;

    @Name("Pos Decimal places")
    @Range(max = 10)
    public Integer posDecimalPlaces = 6;
    @Name("Yaw Decimal places")
    @Range(max = 10)
    public Integer yawDecimalPlaces = 6;
    @Name("Number of Random Digits ")
    @Range(max = 100)
    public Integer numRandomDigits = 10;
}
