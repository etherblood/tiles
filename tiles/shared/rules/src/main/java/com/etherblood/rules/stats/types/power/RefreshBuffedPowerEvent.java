package com.etherblood.rules.stats.types.power;

import com.etherblood.events.Event;
import com.etherblood.rules.stats.PokemonTypes;

/**
 *
 * @author Philipp
 */
public class RefreshBuffedPowerEvent extends Event {

    public int target;
    public PokemonTypes type;

    public RefreshBuffedPowerEvent(int target, PokemonTypes type) {
        this.target = target;
        this.type = type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{target=" + target + ", type=" + type + '}';
    }

}
