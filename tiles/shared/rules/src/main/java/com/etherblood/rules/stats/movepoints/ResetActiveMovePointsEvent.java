package com.etherblood.rules.stats.movepoints;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveMovePointsEvent extends Event implements HasEntity {

    public int target;

    public ResetActiveMovePointsEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{target=" + target + '}';
    }

    @Override
    public int entity() {
        return target;
    }

}
