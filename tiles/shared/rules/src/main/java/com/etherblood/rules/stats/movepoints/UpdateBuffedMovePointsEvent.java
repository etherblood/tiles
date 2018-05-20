package com.etherblood.rules.stats.movepoints;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedMovePointsEvent extends Event implements HasEntity, HasValue {

    public int target, movePoints;

    public UpdateBuffedMovePointsEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "UpdateBuffedMovePointsEvent{" + "target=" + target + ", movePoints=" + movePoints + '}';
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
