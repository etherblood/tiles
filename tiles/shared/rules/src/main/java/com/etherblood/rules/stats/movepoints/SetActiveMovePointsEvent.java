package com.etherblood.rules.stats.movepoints;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class SetActiveMovePointsEvent extends Event implements HasEntity, HasValue {

    public int target, movePoints;

    public SetActiveMovePointsEvent(int target, int movePoints) {
        this.target = target;
        this.movePoints = movePoints;
    }

    @Override
    public String toString() {
        return "SetActiveMovePointsEvent{" + "target=" + target + ", movePoints=" + movePoints + '}';
    }

    @Override
    public int entity() {
        return target;
    }

    @Override
    public int value() {
        return movePoints;
    }

}
