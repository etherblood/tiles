package com.etherblood.rules.stats.health;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedHealthEvent extends Event {

    public int target, health;

    public UpdateBuffedHealthEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "UpdateBuffedHealthEvent{" + "target=" + target + ", health=" + health + '}';
    }

}
