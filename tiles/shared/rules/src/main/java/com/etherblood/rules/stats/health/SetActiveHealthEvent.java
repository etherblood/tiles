package com.etherblood.rules.stats.health;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class SetActiveHealthEvent extends Event implements HasEntity, HasValue {

    public int target, health;

    public SetActiveHealthEvent(int target, int health) {
        this.target = target;
        this.health = health;
    }

    @Override
    public int entity() {
        return target;
    }

    @Override
    public int value() {
        return health;
    }

    @Override
    public String toString() {
        return SetActiveHealthEvent.class.getSimpleName() + "{target=" + target + ", health=" + health + '}';
    }

}
