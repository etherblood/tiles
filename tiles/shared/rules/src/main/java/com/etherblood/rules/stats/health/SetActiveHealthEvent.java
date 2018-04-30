package com.etherblood.rules.stats.health;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class SetActiveHealthEvent extends Event {

    public int target, health;

    public SetActiveHealthEvent(int target, int health) {
        this.target = target;
        this.health = health;
    }

    @Override
    public String toString() {
        return "SetActiveHealthEvent{" + "target=" + target + ", health=" + health + '}';
    }

}
