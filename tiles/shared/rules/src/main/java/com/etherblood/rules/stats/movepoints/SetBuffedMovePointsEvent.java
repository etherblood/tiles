package com.etherblood.rules.stats.movepoints;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class SetBuffedMovePointsEvent extends Event implements HasEntity, HasValue {

    public int target, movePoints;

    public SetBuffedMovePointsEvent(int target, int movePoints) {
        this.target = target;
        this.movePoints = movePoints;
    }

    @Override
    public String toString() {
        return SetBuffedMovePointsEvent.class.getSimpleName() + "{" + "target=" + target + ", movePoints=" + movePoints + '}';
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
