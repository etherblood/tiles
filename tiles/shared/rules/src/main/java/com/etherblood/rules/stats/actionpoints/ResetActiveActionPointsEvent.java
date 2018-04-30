package com.etherblood.rules.stats.actionpoints;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class ResetActiveActionPointsEvent extends Event {

    public int target;

    public ResetActiveActionPointsEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "ResetActiveActionPointsEvent{" + "target=" + target + '}';
    }

}
