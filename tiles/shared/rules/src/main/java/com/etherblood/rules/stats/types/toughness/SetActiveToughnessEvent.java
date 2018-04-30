package com.etherblood.rules.stats.types.toughness;

import com.etherblood.events.Event;
import com.etherblood.rules.stats.PokemonTypes;

/**
 *
 * @author Philipp
 */
public class SetActiveToughnessEvent extends Event {

    public int target, power;
    public PokemonTypes type;

    public SetActiveToughnessEvent(int target, PokemonTypes type, int power) {
        this.target = target;
        this.type = type;
        this.power = power;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{target=" + target + ", power=" + power + ", type=" + type + '}';
    }

}
