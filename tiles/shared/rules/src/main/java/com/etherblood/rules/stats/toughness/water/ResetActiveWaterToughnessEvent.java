package com.etherblood.rules.stats.toughness.water;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveWaterToughnessEvent extends Event implements HasEntity {

    public int entity;

    public ResetActiveWaterToughnessEvent(int entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return ResetActiveWaterToughnessEvent.class.getSimpleName() + "{entity=" + entity + '}';
    }

    @Override
    public int entity() {
        return entity;
    }

}
