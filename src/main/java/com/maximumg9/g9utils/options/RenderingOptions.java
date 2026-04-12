package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.CylinderRenderHelper;
import com.maximumg9.g9utils.config.Config;
import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Options;
import com.maximumg9.g9utils.config.Range;

public class RenderingOptions implements Options {
    public static Config<RenderingOptions> create() {
        return new Config<>(null, RenderingOptions::new);
    }

    @Name("Always Render Block Entities")
    public Boolean alwaysRenderBlockEntities = false;

    @Name("Override Sky Angle")
    public Boolean overrideSkyAngle = false;

    @Range(min=-90,max=90)
    @Name("Sky Angle X")
    public Float skyAngleX = 0f;
    @Range(min=-180,max=180)
    @Name("Sky Angle Y")
    public Float skyAngleY = 0f;

    @Range(max=10)
    @Name("Sun Scale")
    public Float sunScale = 0f;

    @Name("Enable Cylindrical Screenshot with Shift")
    public Boolean cylindricalScreenshot = false;

    @Range(min=3,max=CylinderRenderHelper.MAX_CYLINDER_DIVISIONS)
    @Name("Cylindrical Divisions")
    public Integer cylindricalDivisions = 3;
}
