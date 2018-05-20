package com.etherblood.rules.stats.power.water;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedWaterPowerEvent extends Event implements HasEntity, HasValue {

    public int entity, value;

    public UpdateBuffedWaterPowerEvent(int entity) {
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
        return UpdateBuffedWaterPowerEvent.class.getSimpleName() + "{target=" + entity + ", health=" + value + '}';
    }

}
