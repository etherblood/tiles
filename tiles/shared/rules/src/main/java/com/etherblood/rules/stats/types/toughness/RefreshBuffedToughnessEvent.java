package com.etherblood.rules.stats.types.toughness;

import com.etherblood.events.Event;
import com.etherblood.rules.stats.PokemonTypes;

/**
 *
 * @author Philipp
 */
public class RefreshBuffedToughnessEvent extends Event {

    public int target;
    public PokemonTypes type;

    public RefreshBuffedToughnessEvent(int target, PokemonTypes type) {
        this.target = target;
        this.type = type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{target=" + target + ", type=" + type + '}';
    }

}
