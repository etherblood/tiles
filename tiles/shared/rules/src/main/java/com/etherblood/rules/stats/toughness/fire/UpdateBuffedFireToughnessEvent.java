package com.etherblood.rules.stats.toughness.fire;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedFireToughnessEvent extends Event implements HasEntity, HasValue {

    public int entity, value;

    public UpdateBuffedFireToughnessEvent(int entity) {
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
        return UpdateBuffedFireToughnessEvent.class.getSimpleName() + "{target=" + entity + ", health=" + value + '}';
    }

}
