package com.etherblood.rules.stats.power.fire;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedFirePowerEvent extends Event implements HasEntity, HasValue {

    public int entity, value;

    public UpdateBuffedFirePowerEvent(int entity) {
        this.entity = entity;
    }

    @Override
    public int entity() {
        return entity;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return UpdateBuffedFirePowerEvent.class.getSimpleName() + "{target=" + entity + ", health=" + value + '}';
    }

}
