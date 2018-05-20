package com.etherblood.rules.stats.health;

import com.etherblood.events.Event;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveHealthEvent extends Event implements HasEntity {

    public int target;

    public ResetActiveHealthEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "ResetActiveHealthEvent{" + "target=" + target + '}';
    }

    @Override
    public int entity() {
        return target;
    }

}
