package com.etherblood.rules.stats.actionpoints;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class SetActiveActionPointsEvent extends Event {

    public int target, actionPoints;

    public SetActiveActionPointsEvent(int target, int actionPoints) {
        this.target = target;
        this.actionPoints = actionPoints;
    }

    @Override
    public String toString() {
        return "SetActiveActionPointsEvent{" + "target=" + target + ", actionPoints=" + actionPoints + '}';
    }

}
