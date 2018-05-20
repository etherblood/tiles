package com.etherblood.rules.stats.actionpoints;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedActionPointsEvent extends Event implements HasEntity, HasValue {

    public int target, actionPoints;

    public UpdateBuffedActionPointsEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "UpdateBuffedActionPointsEvent{" + "target=" + target + ", actionPoints=" + actionPoints + '}';
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
