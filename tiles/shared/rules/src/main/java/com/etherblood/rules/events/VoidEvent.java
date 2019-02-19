package com.etherblood.rules.events;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class VoidEvent extends Event {

    VoidEvent(int id) {
        super(id);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VoidEvent && this.id == ((VoidEvent) obj).id;
    }

    @Override
    public int hashCode() {
        return 7;
    }

    @Override
    public String toString() {
        return VoidEvent.class.getSimpleName() + "{id=" + id + "}";
    }

}
