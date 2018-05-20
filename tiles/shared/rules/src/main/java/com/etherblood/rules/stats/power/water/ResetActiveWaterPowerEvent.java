package com.etherblood.rules.stats.power.water;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveWaterPowerEvent extends Event implements HasEntity {

    public int entity;

    public ResetActiveWaterPowerEvent(int entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return ResetActiveWaterPowerEvent.class.getSimpleName() + "{entity=" + entity + '}';
    }

    @Override
    public int entity() {
        return entity;
    }

}
