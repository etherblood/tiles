package com.etherblood.rules.stats.power.air;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveAirPowerEvent extends Event implements HasEntity {

    public int entity;

    public ResetActiveAirPowerEvent(int entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return ResetActiveAirPowerEvent.class.getSimpleName() + "{entity=" + entity + '}';
    }

    @Override
    public int entity() {
        return entity;
    }

}
