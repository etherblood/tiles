package com.etherblood.rules.stats.movepoints;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class ResetActiveMovePointsEvent extends Event {

    public int target;

    public ResetActiveMovePointsEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{target=" + target + '}';
    }

}
