package com.etherblood.rules.movement;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class SetPositionEvent extends Event {

    public int target, position;

    public SetPositionEvent(int target, int position) {
        this.target = target;
        this.position = position;
    }

    @Override
    public String toString() {
        return "SetPositionEvent{" + "target=" + target + ", position=" + Coordinates.toString(position) + '}';
    }
}
