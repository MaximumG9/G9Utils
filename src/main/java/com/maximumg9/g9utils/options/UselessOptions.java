package com.maximumg9.g9utils.options;

import com.maximumg9.g9utils.config.Name;
import com.maximumg9.g9utils.config.Options;
import com.maximumg9.g9utils.config.Range;

public class UselessOptions implements Options {
    @Name("See square grid")
    public Boolean seeSquareGrid = false;

    @Range(min=2,max=16)
    @Name("Square Grid Size")
    public Integer squareGridSize = 9;
    @Name("Add Random Digits to F3")
    public Boolean addRandomDigitsToF3 = false;
    @Name("Number of Random Digits ")
    @Range(min=1,max=1000)
    public Integer numRandomDigits = 10;
}
