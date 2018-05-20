package com.etherblood.rules.stats.power.earth;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveEarthPowerEvent extends Event implements HasEntity {

    public int entity;

    public ResetActiveEarthPowerEvent(int entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return ResetActiveEarthPowerEvent.class.getSimpleName() + "{entity=" + entity + '}';
    }

    @Override
    public int entity() {
        return entity;
    }

}
