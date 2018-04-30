package com.etherblood.rules.stats.health;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class ResetActiveHealthEvent extends Event {

    public int target;

    public ResetActiveHealthEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "ResetActiveHealthEvent{" + "target=" + target + '}';
    }

}
