package com.etherblood.rules.stats.movepoints;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedMovePointsEvent extends Event {

    public int target, movePoints;

    public UpdateBuffedMovePointsEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "UpdateBuffedMovePointsEvent{" + "target=" + target + ", movePoints=" + movePoints + '}';
    }

}
