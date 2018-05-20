package com.etherblood.rules.stats.power.fire;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveFirePowerEvent extends Event implements HasEntity {

    public int entity;

    public ResetActiveFirePowerEvent(int entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return ResetActiveFirePowerEvent.class.getSimpleName() + "{entity=" + entity + '}';
    }

    @Override
    public int entity() {
        return entity;
    }

}
