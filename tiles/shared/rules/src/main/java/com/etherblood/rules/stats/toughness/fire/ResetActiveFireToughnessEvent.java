package com.etherblood.rules.stats.toughness.fire;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveFireToughnessEvent extends Event implements HasEntity {

    public int entity;

    public ResetActiveFireToughnessEvent(int entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return ResetActiveFireToughnessEvent.class.getSimpleName() + "{entity=" + entity + '}';
    }

    @Override
    public int entity() {
        return entity;
    }

}
