package com.etherblood.rules.abilities;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class Action {

    public final Event event;

    public Action(Event event) {
        this.event = event;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.event.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Action)) {
            return false;
        }
        Action other = (Action) obj;
        return event.equals(other.event);
    }
}
