package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Options;
import com.maximumg9.g9utils.config.Range;

@SuppressWarnings("CanBeFinal")
public class HUDOptions implements Options {
    @Name("See Cos and Sin of Yaw")
    public Boolean seeCosAndSinForYaw = false;
    @Name("See Yaw in Radians")
    public Boolean seeRadianRoundedYaw = false;
    @Name("See Unclamped Yaw")
    public Boolean seeAccurateYaw = false;

    @Name("See onGround")
    public Boolean seeOnGround = false;
    @Name("See Pos")
    public Boolean seePos = false;

    @Name("See Velocity")
    public Boolean seeVel = false;
    @Name("See Hit Type")
    public Boolean seeSwordHitType = false;

    @Name("Pos Decimal places")
    @Range(max = 32)
    public Integer posDecimalPlaces = 6;
    @Name("Yaw Decimal places")
    @Range(max = 32)
    public Integer yawDecimalPlaces = 6;
    @Name("See Server Side Sprint (approx.)")
    public Boolean seeServerSideSprint = false;
    @Name("See Attribute Swaps")
    public Boolean seeAttributeSwaps = false;
    @Name("Late/Early Attribute Swap Timeout")
    @Range(min=0,max=20)
    public Integer attributeSwapTimeout = 5;
}
