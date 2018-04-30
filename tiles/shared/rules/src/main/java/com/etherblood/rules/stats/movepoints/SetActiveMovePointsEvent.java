package com.etherblood.rules.stats.movepoints;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class SetActiveMovePointsEvent extends Event {

    public int target, movePoints;

    public SetActiveMovePointsEvent(int target, int movePoints) {
        this.target = target;
        this.movePoints = movePoints;
    }

    @Override
    public String toString() {
        return "SetActiveMovePointsEvent{" + "target=" + target + ", movePoints=" + movePoints + '}';
    }

}
