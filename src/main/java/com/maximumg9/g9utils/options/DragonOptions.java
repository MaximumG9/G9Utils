package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.Config;
import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Options;
import com.maximumg9.g9utils.config.Range;

@SuppressWarnings("CanBeFinal")
public class DragonOptions implements Options {
    public static Config<DragonOptions> create() {
        return new Config<>(null, DragonOptions::new);
    }

    @Name("Show Special Dragon Boxes")
    public Boolean showDragonHitboxes = false;

    @Range(max=255)
    @Name("Knockback Hitbox Red")
    public Integer kbRed = 255;
    @Range(max=255)
    @Name("Knockback Hitbox Green")
    public Integer kbGreen = 128;
    @Range(max=255)
    @Name("Knockback Hitbox Blue")
    public Integer kbBlue = 0;

    @Range(max=255)
    @Name("Damage Hitbox Red")
    public Integer dmgRed = 255;
    @Range(max=255)
    @Name("Damage Hitbox Green")
    public Integer dmgGreen = 0;
    @Range(max=255)
    @Name("Damage Hitbox Blue")
    public Integer dmgBlue = 0;

    @Range(max=255)
    @Name("Body Center Red")
    public Integer centerRed = 255;
    @Range(max=255)
    @Name("Body Center Green")
    public Integer centerGreen = 255;
    @Range(max=255)
    @Name("Body Center Blue")
    public Integer centerBlue = 255;

    @Range(max=255)
    @Name("Optimal Circle Red")
    public Integer optimalRed = 0;
    @Range(max=255)
    @Name("Optimal Circle Green")
    public Integer optimalGreen = 255;
    @Range(max=255)
    @Name("Optimal Circle Blue")
    public Integer optimalBlue = 0;
}
