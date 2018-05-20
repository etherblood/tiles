package com.etherblood.rules.stats.toughness.earth;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class SetActiveEarthToughnessEvent extends Event implements HasEntity, HasValue {

    public int entity, value;

    public SetActiveEarthToughnessEvent(int entity, int value) {
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
        return SetActiveEarthToughnessEvent.class.getSimpleName() + "{entity=" + entity + ", value=" + value + '}';
    }

}
