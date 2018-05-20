package com.etherblood.rules.stats.actionpoints;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class SetActiveActionPointsEvent extends Event implements HasEntity, HasValue {

    public int target, actionPoints;

    public SetActiveActionPointsEvent(int target, int actionPoints) {
        this.target = target;
        this.actionPoints = actionPoints;
    }

    @Override
    public String toString() {
        return "SetActiveActionPointsEvent{" + "target=" + target + ", actionPoints=" + actionPoints + '}';
    }

    @Override
    public int entity() {
        return target;
    }

    @Override
    public int value() {
        return actionPoints;
    }

}
