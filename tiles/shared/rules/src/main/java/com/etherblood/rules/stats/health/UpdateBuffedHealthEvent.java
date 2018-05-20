package com.etherblood.rules.stats.health;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedHealthEvent extends Event implements HasEntity, HasValue {

    public int target, health;

    public UpdateBuffedHealthEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "UpdateBuffedHealthEvent{" + "target=" + target + ", health=" + health + '}';
    }

    @Override
    public int entity() {
        return target;
    }

    @Override
    public int value() {
        return health;
    }

}
