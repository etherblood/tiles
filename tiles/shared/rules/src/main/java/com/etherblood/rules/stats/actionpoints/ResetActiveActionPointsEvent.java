package com.etherblood.rules.stats.actionpoints;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveActionPointsEvent extends Event implements HasEntity {

    public int target;

    public ResetActiveActionPointsEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "ResetActiveActionPointsEvent{" + "target=" + target + '}';
    }

    @Override
    public int entity() {
        return target;
    }

}
