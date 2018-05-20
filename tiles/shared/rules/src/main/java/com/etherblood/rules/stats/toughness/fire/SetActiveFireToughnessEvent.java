package com.etherblood.rules.stats.toughness.fire;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class SetActiveFireToughnessEvent extends Event implements HasEntity, HasValue {

    public int entity, value;

    public SetActiveFireToughnessEvent(int entity, int value) {
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
        return SetActiveFireToughnessEvent.class.getSimpleName() + "{entity=" + entity + ", value=" + value + '}';
    }

}
