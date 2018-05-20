package com.etherblood.rules.stats.toughness.earth;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveEarthToughnessEvent extends Event implements HasEntity {

    public int entity;

    public ResetActiveEarthToughnessEvent(int entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return ResetActiveEarthToughnessEvent.class.getSimpleName() + "{entity=" + entity + '}';
    }

    @Override
    public int entity() {
        return entity;
    }

}
