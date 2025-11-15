package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.Config;
import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Options;
import com.maximumg9.g9utils.config.Range;

public class UselessOptions implements Options {
    public static Config<UselessOptions> create() {
        return new Config<>(null, UselessOptions::new);
    }

    @Name("See square grid")
    public Boolean seeSquareGrid = false;

    @Range(min=2,max=16)
    @Name("Square Grid Size")
    public Integer squareGridSize = 9;
}
