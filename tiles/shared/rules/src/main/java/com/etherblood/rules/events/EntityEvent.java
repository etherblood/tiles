package com.etherblood.rules.events;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class EntityEvent extends Event {

    public final int entity;

    EntityEvent(int id, int entity) {
        super(id);
        this.entity = entity;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.entity;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntityEvent)) {
            return false;
        }
        EntityEvent other = (EntityEvent) obj;
        return this.entity == other.entity;
    }

    @Override
    public String toString() {
        return EntityEvent.class.getSimpleName() + "{entity=" + entity + ", id=" + id + '}';
    }
}
