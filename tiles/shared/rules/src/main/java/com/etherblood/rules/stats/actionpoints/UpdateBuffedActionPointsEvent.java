package com.etherblood.rules.stats.actionpoints;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedActionPointsEvent extends Event {

    public int target, actionPoints;

    public UpdateBuffedActionPointsEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "UpdateBuffedActionPointsEvent{" + "target=" + target + ", actionPoints=" + actionPoints + '}';
    }

}
