package com.etherblood.rules.context;

/**
 *
 * @author Philipp
 */
public class ElementComponentMaps {

    public final StatComponentMaps power;
    public final StatComponentMaps toughness;

    public ElementComponentMaps(StatComponentMaps power, StatComponentMaps toughness) {
        this.power = power;
        this.toughness = toughness;
    }
}
