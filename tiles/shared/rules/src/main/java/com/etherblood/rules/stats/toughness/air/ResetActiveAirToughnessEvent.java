package com.etherblood.rules.stats.toughness.air;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveAirToughnessEvent extends Event implements HasEntity {

    public int entity;

    public ResetActiveAirToughnessEvent(int entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return ResetActiveAirToughnessEvent.class.getSimpleName() + "{entity=" + entity + '}';
    }

    @Override
    public int entity() {
        return entity;
    }

}
