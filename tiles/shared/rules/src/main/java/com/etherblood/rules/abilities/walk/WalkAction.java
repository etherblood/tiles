package com.etherblood.rules.abilities.walk;

import com.etherblood.events.Event;
import com.etherblood.rules.movement.Coordinates;

/**
 *
 * @author Philipp
 */
public class WalkAction extends Event {

    public int actor, from, to;

    public WalkAction(int actor, int from, int to) {
        this.actor = actor;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "WalkAction{actor=" + actor + ", from=" + Coordinates.toString(from) + ", to=" + Coordinates.toString(to) + '}';
    }
}
