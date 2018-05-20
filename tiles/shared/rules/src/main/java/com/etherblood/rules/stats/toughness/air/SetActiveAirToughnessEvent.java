package com.etherblood.rules.stats.toughness.air;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class SetActiveAirToughnessEvent extends Event implements HasEntity, HasValue {

    public int entity, value;

    public SetActiveAirToughnessEvent(int entity, int value) {
        this.entity = entity;
        this.value = value;
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
        return SetActiveAirToughnessEvent.class.getSimpleName() + "{entity=" + entity + ", value=" + value + '}';
    }

}
